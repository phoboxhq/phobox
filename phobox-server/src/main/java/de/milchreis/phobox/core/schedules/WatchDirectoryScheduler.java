package de.milchreis.phobox.core.schedules;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.milchreis.phobox.db.entities.Item;
import de.milchreis.phobox.db.repositories.ItemRepository;
import de.milchreis.phobox.exceptions.HashGenerationException;
import de.milchreis.phobox.utils.storage.HashUtil;
import org.apache.commons.io.FileUtils;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.file.FileAction;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.file.LoopInfo;
import de.milchreis.phobox.core.model.PhoboxModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WatchDirectoryScheduler extends TimerTask implements FileAction, Schedulable {

	private int lastSize = 0;
	private int intervalInMillis;
	private Timer timer;
	private boolean ready;

	private ItemRepository itemRepository;
	private Map<String, Boolean> checkFiles = new HashMap<>();

	public WatchDirectoryScheduler(int intervalInMillis, ItemRepository itemRepository) {
		timer = new Timer();
		this.intervalInMillis = intervalInMillis;
		this.itemRepository = itemRepository;

		// Clean up the checkFiles map
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				checkFiles.clear();
			}
		}, 0, TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS));
	}

	@Override
	public void run() {
		ready = false;

		PhoboxModel model = Phobox.getModel();
		File watchDir = model.getWatchPath();

		// Skip here if no watchDir is defined
		if(watchDir == null) return;

		if(!watchDir.exists())
			watchDir.mkdirs();

		new FileProcessor().foreachFile(
				watchDir,
				PhoboxDefinitions.SUPPORTED_IMPORT_FORMATS,
				this,
				true);

		ready = true;
	}
	
	@Override
	public void process(File file, LoopInfo info) {
		try {
			if(!checkFiles.containsKey(file.getAbsolutePath())) {
				String hash = HashUtil.sha1File(file);

				Item item = itemRepository.findByHash(hash);

				if(item != null) {
					FileUtils.copyFileToDirectory(file, Phobox.getModel().getIncomingPath());
				}

				checkFiles.put(file.getAbsolutePath(), true);
			}

		} catch (IOException e) {
			log.error("Could not copy file: " + file.getName());
		} catch (HashGenerationException e) {
			log.error("Could hash file: " + file.getName());
		}
	}

	public void start() {
		timer.schedule(this, 100, intervalInMillis);
	}
	
	public void stop() {
		timer.cancel();
	}

	@Override
	public boolean isReady() {
		return ready;
	}

}
