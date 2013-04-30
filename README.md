# NGSV: Next Generation Sequencer Viewer

__NGSV__ is a novel viewer for data of Next-generation DNA sequencing (NGS).
It visualizes them 3-dimentionally and provides us seamless zooming and moving for browsing the data.
It has a database inside the system and uses OpenGL for rendering.

# Requirements

* Java (later than 1.6SE)
    * casmi (ver. 0.3.2)
    * logback
    * Java-WebSocket (by TooTallNate)
    * ini4j
* MySQL
* NGSV Console

# Setup viewer configulation

Setup database settings.

```
$ cp config/config.ini.example config/config.ini

db.host     = [hostname]
db.database = ngsv
db.user     = [db_user]
db.password = [db_password]
```

# Usage

1. Select sam files, bed files, a chromosome name, and region (and a gene name optionally). And click 'OK'.
2. Drag left or right to scroll the view.
3. With mouse wheel scrolling, you can change scale of view.
4. With mouseover, you can see detail information of elements.
5. 'Space + drag' to display 3-D view. 'r' to reset the view.
6. 'c' to re-select dataset.

# Package

Build main project and package as executable file (.app/.exe).

## Mac OS X

    $ mvn -P production-mac -Dmaven.test.skip=true package

## Windows

    $ mvn -P production-win -Dmaven.test.skip=true package

 Copy native jar files to the exe directory.

     $ cp [jogl-all-2.0-rc9-natives-windows-i586.jar] [exe directory]
     $ cp [gluegen-all-2.0-rc9-natives-windows-i586.jar] [exe directory]

# License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
