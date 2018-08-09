# ExifReader
Tool for reading image file exif data and inserting it into a local SQLite instance.

### Prerequisites (macOS)
```
$ brew install exiftool
$ brew install sqlite
```

### Usage
```
$ ./run.sh "-p <path>"
```

### Examples (macOS)

To read exif data from all crw, cr2, and arw files in the ~/Pictures directory:
```
$ ./run.sh "-p ~/Pictures"
```
