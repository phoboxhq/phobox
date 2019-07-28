
export default {

	messages: {
		en: {
			menu: {
				pictures: 'Pictures',
				approval: 'Picture approval',
				search: 'Search for pictures',
				statistics: 'Statistics',
				albums: 'Albums',
				status: 'Status',
				upload: 'Upload',
				settings: 'Settings',
				about: 'About',
			},

			approval: {
				accept: 'Accept (import)',
				decline: 'Decline (delete)',
			},

			pictures: {
				of: 'of',
				not_found: 'No pictures found.',
				go_back: 'Go back',
				download: 'Download',
				rename: 'Rename',
				delete: 'Delete',
				tags: 'TAGs',
				album: 'To album',
				album_remove: 'Remove from album',
				waitforthumb: 'Creating thumbnail... Please reload.',
				load_more: 'Load more ...',
				reload: 'Currently the directory will be processed. Please reload',

				rename_dialog_head: 'Rename element',
				rename_dialog_save: 'Save',
				rename_dialog_cancel: 'Cancel',
				rename_dialog_failed: 'File could not be renamed. Maybe the file already exists.',

				delete_dialog_head: 'Delete element',
				delete_dialog_content: 'Do you want to delete the following element?',
				delete_dialog_delete: 'Delete',
				delete_dialog_cancel: 'Cancel',
				delete_dialog_failed: 'Element could not be deleted.',

				favorite_dialog_head: 'Add file to album',
				favorite_dialog_new: 'New album',
				favorite_dialog_existing: 'Existing album',
				favorite_dialog_name: 'New album name',
				favorite_dialog_select: 'Select album',
				favorite_dialog_add: 'Add to album',

				iso: 'ISO',
				exposure: 'Exposure time',
				aperture: 'Aperture',
				focalLength: 'Focal length',
			},

			album: {
				select: 'Select album',
				download: 'download album',
				
				delete_confirm_headline: 'Delete album:',
				delete_confirm_text: 'Do you really want to delete the selected album?',
				delete_cancel: 'Cancel',
				
				delete: 'remove album',
				delete_info: 'All pictures of the album are preserved, only the album is removed.',
				
				rename: 'rename album',
				rename_newname: 'New album name',
				rename_ok: 'Rename',
				rename_cancel: 'Cancel',

				empty: 'There are no albums yet.',
				empty_help: 'Find a picture and click on the star <i class="fa fa-star" aria-hidden="true"></i> to create an album.',
			},

			search: {
				searchbar: 'Search for name, TAG, date ...',
				search_button: 'Search',
				no_results: 'No files found.',
			},

			statistics: {
				headline: 'Statistics',
				month_names: [
					"January",
					"February",
					"March",
					"April",
					"May",
					"June",
					"July",
					"August",
					"September",
					"October",
					"November",
					"December"
				],
				overview_year: 'year overview',
				load: 'load',
				tagging_state: 'Tagging state',
				tagged: 'Images with TAGs',
				untagged: 'Images without TAGs',
			},

			status: {
				import_state: 'Import state',
				remaining_files: 'Remaining files',
				current_file: 'Current file',
				free_space: 'Free space',
				max_space: 'Max space',
				number_of_pictures: 'Number of pictures',
				uptime: 'Uptime',
				hours: 'hours',
				days: 'days',
			},

			upload: {
				drop_files: 'Drop files here to upload',
			},

			settings: {
				saved: 'Saved',
				failed: 'Error',

				access_auth_head: 'Security: Access by authentication',
				access_auth_description: 'Set up a username and password for a basic authenication before you access phobox. If your wireless network is open for other user, you can secure the access to your photos.',
				access_auth_username: 'Username',
				access_auth_password: 'Password',
				access_auth_save: 'Save credentials',
				access_auth_reset: 'Delete credentials',
				access_auth_success: 'Restart the application to activate this setting.',
				access_auth_failed: 'New settings could not saved.',

				import_pattern_head: 'Import: Directory format',
				import_pattern_description: 'Define a import format for all new incoming files. Use markers and directory separator to configure the format for the target directory. The data will be catched from the EXIF data of the picture.',
				import_pattern_keywords: `
					<ul>
						<li>%Y - Use it for the year of creation of the picture (f.e. 2020)</li>
						<li>%M - Use it for the month of creation of the picture (f.e. 12)</li>
						<li>%D - Use it for the day of creation of the picture (f.e. 24)</li>
						<li> / - Use it for the directory separation, if you want to create subdirectories</li>
					</ul>`,
				import_pattern_formatting: 'Import formatting',
				import_pattern_save: 'Save pattern',

				storage_thumbnails_head: 'Storage: Recreate thumbnails',
				storage_thumbnails_description: 'Recreates the thumbnail pictures and updates the internal database.',
				storage_thumbnails_start: 'Start recreation now',
				storage_thumbnails_success: 'All new incoming files will be saved with the new format.',
				storage_thumbnails_failed: 'New settings could not saved.',

				storage_path_head: 'Storage: Change storage main path',
				storage_path_description: 'Change the main path to your stored pictures. Phobox need an existing absolute path to the new storage directory.',
				storage_path_start: 'Save path',
				storage_path_success: 'Path successful changed',
				storage_path_failed: 'Path could not changed',
			},

			about: {
				head: 'About Phobox',
				background_head: 'Background',
				background_text: 
					`<p>I built phobox to solve a problem which I had with all existing tools. I want to keep my photos on my network storage to access them in my local wifi with my tablet in the couch. Moreover it should be possible to browse the files on computer in the file explorer.</p>
					<p>All existing solutions didn’t fit my requirements or been expensive. So I begun to build a solution to myself and phobox was born. After a couple of years of development and personal usage I decided to make this project public. Phobox solves the origin problems in my daily photo workflow. I think out there are persons with the same problems, too. </p>
					<p>Phobox is a very special project for me and I will never want to do without it again. But currently the development is completely in my spare time. If you like phobox and you want to support me, please <a href="https://phoboxhq.github.io/">look here.</a></p>
					`,

				author_and_contact_head: 'Author and Contact',
				author_and_contact_text: `<p>Author: Nick 'Milchreis' Müller</p>
					<p>Contact at <a href="https://github.com/phobox/phobox">Github</a></p>`,
				thirdparty_libs: 'Third-Party Software used by Phobox',
				thirdparty_lib_more: 'More details here',
			}
		},

		de: {
			menu: {
				pictures: 'Bilder',
				approval: 'Bilder prüfen',
				search: 'Bildersuche',
				statistics: 'Statistiken',
				albums: 'Alben',
				status: 'Status',
				upload: 'Upload',
				settings: 'Einstellungen',
				about: 'Über Phobox',
			},

			approval: {
				accept: 'Annehmen und importieren',
				decline: 'Verweigern und löschen',
			},

			pictures: {
				of: 'von',
				not_found: 'Keine Bilder gefunden.',
				go_back: 'Zurück',
				download: 'Herunterladen',
				rename: 'Umbenennen',
				delete: 'Löschen',
				tags: 'Schlagworte',
				album: 'Zum Album',
				album_remove: 'Aus Album entfernen',
				waitforthumb: 'Erstelle Vorschaubild... Bitte aktualisieren.',
				load_more: 'Weitere laden ...',
				reload: 'Aktuell wird das Verzeichnis aufgebaut. Bitte neuladen',

				rename_dialog_head: 'Umbenennen von',
				rename_dialog_save: 'Speichern',
				rename_dialog_cancel: 'Abbrechen',
				rename_dialog_failed: 'Datei konnte nicht umbenannt werden. Eventuell existiert die Datei bereits.',

				delete_dialog_head: 'Löschen von',
				delete_dialog_content: 'Soll das folgende Element wirklich gelöscht werden?',
				delete_dialog_delete: 'Löschen',
				delete_dialog_cancel: 'Abbrechen',
				delete_dialog_failed: 'Element konnte nicht gelöscht werden.',

				favorite_dialog_head: 'Datei zu einem Album hinzufügen',
				favorite_dialog_new: 'Neues Album',
				favorite_dialog_existing: 'Vorhandenes Album',
				favorite_dialog_name: 'Name des neuen Albums',
				favorite_dialog_select: 'Album auswählen',
				favorite_dialog_add: 'Hinzufügen',

				iso: 'ISO',
				exposure: 'Belichtungszeit',
				aperture: 'Blende',
				focalLength: 'Brennweite',
			},

			album: {
				select: 'Album auswählen',
				download: 'Album herunterladen',
				
				delete_confirm_headline: 'Album löschen: ',
				delete_confirm_text: 'Soll das ausgewählte Album wirklich gelöscht werden?',
				delete_cancel: 'Abbrechen',
				
				delete: 'Album entfernen',
				delete_info: 'Alle Bilder des Albums bleiben erhalten, nur das Album wird entfernt.',

				rename: 'Album umbenennen',
				rename_newname: 'Neuer Albumname',
				rename_ok: 'Umbenennen',
				rename_cancel: 'Abbrechen',
				
				empty: 'Es gibt noch kein Album.',
				empty_help: 'Suche dir ein Bild und klicke auf den Stern <i class="fa fa-star" aria-hidden="true"></i> um ein Album zu erzeugen.'
			},

			search: {
				searchbar: 'Suche nach Namen, Schlagworten, Datum ...',
				search_button: 'Suchen',
				no_results: 'Keine Dateien gefunden.',
			},

			statistics: {
				headline: 'Statistiken',
				month_names: [
					"Januar",
					"Februar",
					"März",
					"April",
					"Mai",
					"Juni",
					"Juli",
					"August",
					"September",
					"Oktober",
					"November",
					"Dezember"
				],
				overview_year: 'Jahresübersicht',
				load: 'Laden',
				tagging_state: 'Verschlagwortung',
				tagged: 'Bilder mit Schlagworten',
				untagged: 'Bilder ohne Schlagworte',
			},

			status: {
				import_state: 'Importstatus',
				remaining_files: 'Wartende Dateien',
				current_file: 'Bearbeitete Datei',
				free_space: 'Freier Speicher',
				max_space: 'Gesamtspeicher',
				number_of_pictures: 'Bilderanzahl',
				uptime: 'Laufzeit',
				hours: 'Stunden',
				days: 'Tage',
			},

			upload: {
				drop_files: 'Klick oder Ziehe Datei hier zum hochladen',
			},

			settings: {
				saved: 'Gespeichert',
				failed: 'Fehler',

				access_auth_head: 'Sicherheit: Zugang mit Nutzerauthentifizierung',
				access_auth_description: 'Lege einen Benutzernamen und ein Password fest, um den Zugang zur Phobox zu sichern. Wenn dein WLAN zugänglich für andere Nutzer ist, kannst du so deine Bilder schützen.',
				access_auth_username: 'Benutzername',
				access_auth_password: 'Passwort',
				access_auth_save: 'Logindaten speichern',
				access_auth_reset: 'Logindaten löschen',
				access_auth_success: 'Anwendung muss neugestartet werden, damit die Einstellung funktioniert.',
				access_auth_failed: ' Einstellung konnte nicht gespeichert werden.',

				import_pattern_head: 'Import: Verzeichnissturktur',
				import_pattern_description: 'Lege ein Importformat für alle neu hinzugefügten Dateien fest. Mit der Verwendung von Markern und Verzeichnis-Trennern kann die Zielverzeichnisstruktur konfiguriert werden. Die Daten werden aus den EXIF-Informationen der Bilder gelesen.',
				import_pattern_keywords: `
					<ul>
						<li>%Y - Benutze für das Erstellungsjahr des Bildes (z.B. 2020)</li>
						<li>%M - Benutze für den Erstellungsmonat des Bildes (z.B. 12)</li>
						<li>%D - Benutze für den Erstellungstag des Bildes (z.B. 24)</li>
						<li> / - Benutze als Verzeichnistrenner, wenn Unterverzeichnisse erzeugt werden sollen</li>
					</ul>`,
				import_pattern_formatting: 'Format',
				import_pattern_save: 'Konfiguration speichern',

				storage_thumbnails_head: 'Speicher: Vorschaubilder erzeugen',
				storage_thumbnails_description: 'Erstellt für alle Bilder die Vorschaubilder erneut und aktualisiert die interne Datenbank.',
				storage_thumbnails_start: 'Vorschaubilder-Erstellung starten',
				storage_thumbnails_success: 'Alle neuen Dateien werden mit den neuen Format gespeichert',
				storage_thumbnails_failed: 'Neue Einstellung konnte nicht gespeichert werden.',

				storage_path_head: 'Speicher: Änderung des Bilderverzeichnisses',
				storage_path_description: 'Ändere hier den Hauptpfad zu deinen gespeicherten Bildern. Phobox benötigt einen vorhandenen absoluten Pfad zu dem neuen Zielverzeichnis.',
				storage_path_start: 'Pfad speichern',
				storage_path_success: 'Pfad erfolgreich geändert',
				storage_path_failed: 'Pfad konnte nicht geändert werden.',
			},

			about: {
				head: 'Über Phobox',
				background_head: 'Hintergrund',
				background_text: 
					`<p>Ich habe Phobox gebaut, um ein Problem zu lösen, das ich mit allen vorhandenen Tools hatte. Ich möchte meine Fotos auf meinem Netzwerkspeicher aufbewahren, um auf sie in meinem lokalen WLAN mit meinem Tablett auf der Couch zuzugreifen. Außerdem sollte es möglich sein, die Dateien auf dem Computer im Datei-Explorer zu durchsuchen.</p>
					<p>Alle vorhandenen Lösungen passten nicht zu meinen Anforderungen oder waren teuer. Also begann ich, eine Lösung für mich selbst zu entwickeln und phobox wurde geboren. Nach ein paar Jahren der Entwicklung und des persönlichen Gebrauchs entschied ich mich, dieses Projekt zu veröffentlichen. Phobox löst die Ursprungsprobleme in meinem täglichen Foto-Workflow. Ich denke, es gibt auch Menschen mit den gleichen Problemen.</p> 
					<p>Phobox ist für mich ein ganz besonderes Projekt und ich will es nie wieder missen wollen. Aber derzeit ist die Entwicklung komplett in meiner Freizeit. Wenn dir die Phobox gefällt und du mich unterstützen willst, schau bitte <a href="https://phoboxhq.github.io/">hier nach.</a></p>
					`,
				author_and_contact_head: 'Autor und Kontakt',
				author_and_contact_text: `<p>Autor: Nick 'Milchreis' Müller</p>
					<p>Kontakt über <a href="https://github.com/phoboxhq/phobox">Github</a></p>`,
				thirdparty_libs: 'Von Phobox verwendete Software von Drittanbietern',
				thirdparty_lib_more: 'Weitere Details gibt es hier',
			}
		},
	},

	getBrowserLocale() {
		let localekey = navigator.language || navigator.userlanguage;
		return localekey.substring(0, 2)
	},
}

