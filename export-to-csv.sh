#!/bin/bash

sqlite3 -batch photographs.db <<"EOF"
.headers on
.mode csv
.output photographs.csv
SELECT * FROM photograph ORDER BY id;
.quit
EOF