# delete directory if exists
rm -rf target
# create directory
mkdir target
# compile the project
javac -d ./target src/java/edu.school21.printer/*/*.java
# copy resources to target
cp -R src/resources target/.
# Assemble JAR-file
jar cfm ./target/images-to-chars-printer.jar src/manifest.txt -C target edu -C target resources
# Reassembly JAR
jar cfm ./target/images-to-chars-printer.jar src/manifest.txt -C target .
# run program
java -jar target/images-to-chars-printer.jar . 0
