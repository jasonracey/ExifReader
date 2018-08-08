# ExifReader
Tool for reading image file exif data and inserting it into a local SQLite instance.

### Prerequisites (macOS)
```
$ brew install exiftool
$ brew install sqlite
```

### Usage
```
$ ./run.sh "-p <path> [extensions]"
```

### Examples (macOS)

To read exif data from all files in the ~/Pictures directory:
```
$ ./run.sh "-p ~/Pictures"
```

To read exif data from only jpg and tif files in the ~/Pictures directory:
```
$ ./run.sh "-p ~/Pictures jpg,tif"
```
