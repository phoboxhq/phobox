
var en = {
	menu: {
		pictures: 'Pictures',
		albums: 'Albums',
		status: 'Status',
		upload: 'Upload',
		settings: 'Settings',
		about: 'About',
	},

	pictures: {
		of: 'of',
		not_found: 'No pictures found.',
		go_back: 'Go back',
		download: 'Download',
		rename: 'Rename',
		delete: 'Delete',
		waitforthumb: 'Creating thumbnail... Please reload.',

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
	},

	status: {
		import_state: 'Import state',
		remaining_files: 'Remaining files',
		current_file: 'Current file',
		free_space: 'Free space',
		max_space: 'Max space'
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

		storage_thumbnails_head: 'Storage: Create thumbnails',
		storage_thumbnails_description: 'Recreates for all images without thumbnails new ones.',
		storage_thumbnails_start: 'Start thumbnail creation now',
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
		background_text: `<p>Phobox is born as a private project to be a lean solution to get a fast and easy access to my personal pictures at home on different devices. It should be a lightweight extention to an existing picture storage and can be used as simple picture organizer, too.</p>
			<p>With Phobox you get a light HTTP-Server which will be provide your images in your private network and your local device. Moreover it's possible to import new images to storage and let be automatically placed in the correct directory struture.</p>`,
	
		author_and_contact_head: 'Author and Contact',
		author_and_contact_text: `<p>Author: Nick Müller</p>
			<p>Contact at <a href="https://github.com/milchreis/phobox">Github</a></p>`,
	}
}

var de = {
	menu: {
		pictures: 'Bilder',
		albums: 'Alben',
		status: 'Status',
		upload: 'Upload',
		settings: 'Einstellungen',
		about: 'Über Phobox',
	},

	pictures: {
		of: 'von',
		not_found: 'Keine Bilder gefunden.',
		go_back: 'Zurück',
		download: 'Herunterladen',
		rename: 'Umbenennen',
		delete: 'Löschen',
		waitforthumb: 'Erstelle Vorschaubild... Bitte aktualisieren.',
		
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
	},

	status: {
		import_state: 'Importstatus',
		remaining_files: 'Wartende Dateien',
		current_file: 'Bearbeitete Datei',
		free_space: 'Freier Speicher',
		max_space: 'Gesamtspeicher'
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
		storage_thumbnails_description: 'Erstellt für alle Bilder die Vorschaubilder erneut, wenn keine vorhanden sind.',
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
		background_text: `<p>Phobox wurde als privates Projekt gestartet, um eine schlanke Lösung für den schnellen und einfachen Zugriff auf die eigenen Bilder zuhause von verschiedenen Geräten zu erhalten. Es soll eine leichtgewichtige Erweiterung von bereits vorhandenen Bildersammlungen sein und auch als einfacher Bildspeicher-Organizer verwendet werden können.</p>
						  <p>Mit Phobox erhält man einen kleinen HTTP-Server, welcher die Bilder in einem lokalen Netzwerk für alle Geräte bereitstellt. Darüber hinaus ist es möglich neue Bilder in die Sammlung hinzufügen und automatisch in die Verzeichnissturktur einsortieren zu lassen.</p>`,
	
		author_and_contact_head: 'Autor und Kontakt',
		author_and_contact_text: `<p>Autor: Nick Müller</p>
			<p>Kontakt über <a href="https://github.com/milchreis/phobox">Github</a></p>`,
	}
}

const Locale = {

	languages: {
		en: en,
		de: de,
	},
	
	values: undefined,

	init: function() {
		this.__detectLang(navigator.language || navigator.userlanguage); 
	},

	setLanguage: function(localekey) {
		this.__detectLang(localekey);
	},

	get: function(key) {
		return this.values.key;
	},

	__detectLang: function(localekey) {

		if(localekey in this.languages) {
			this.values = this.languages[localekey];
		} else {
			this.values = this.languages.en;
		}
	},
}

Locale.init();