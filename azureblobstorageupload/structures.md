# Music List Structure

```bash
music/
|
|-- author-1/
|   |-- album-1_1/
|   |   |-- song-1_1_1
|   |   |-- song-1_1_2
|   |   |-- song-1_1_3
|   |   |-- song-1_1_4
|   |
|   |-- album-1_2/
|   |   |-- song-1_2_1
|   |   |-- song-1_2_2
|   |   |-- song-1_2_3
|   |   |-- song-1_2_4
|   |   |-- song-1_2_5
|   |   |-- song-1_2_6
|   |
|-- author-2/
    |-- album-2_1/
        |-- song-2_1_1
        |-- song-2_1_2
        |-- song-2_1_3
        |-- song-2_1_4
        |-- song-2_1_5
```

```bash
  Storage Account -|- Container_1 -|- Blob_1
                   |               |
                   |               |- Blob_2
                   |
                   |- Container_2 -|- Blob_1
                                   |
                                   |- Blob_2
                                   |
                                   |- Blob_3
  ```
