# Errors on ubuntu with openJDK

Error: On startup `Caused by: java.lang.NoClassDefFoundError: javafx/application/Application`

Solution: `sudo apt install openjfx`

# Errors on Synology NAS on startup

Error: On startup `Caused by: java.lang.NoClassDefFoundError: javafx/application/Application`
Problem: openjfx can not be installed

Solution: 
 * Download oracle-jdk8 (Linux x64 for DSM 218+) and extract
 * run phobox with `./jdk1.8.0_202/bin/java -jar phobox-server-1.0.0.jar -nw`

