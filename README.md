# ExifReader
Tool for reading image file exif data and inserting it into a local SQLite instance.

### Prerequisites (macOS)
```
$ brew install exiftool
$ brew install sqlite
```

### Usage
```
$ ./run.sh "-p <path> [extension(s)]"
```

### Examples (macOS)

To read exif data from all [supported](https://www.sno.phy.queensu.ca/~phil/exiftool/#supported) image files in my local Pictures directory:
```
$ ./run.sh "-p /Users/jasonracey/Pictures"
```

To read exif data from all .arw and .cr2 files in my local Pictures directory:
```
$ ./run.sh "-p /Users/jasonracey/Pictures arw cr2"
```
