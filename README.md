# NGSV: Next Generation Sequencer Viewer

NGSV is a novel viewer for data of Next-generation DNA sequencing (NGS).
It visualizes them 3-dimentionally and provides us seamless zooming and moving for browsing the data.
It has a database inside the system and uses OpenGL for rendering.

# Requirements

* Java (later than 1.6SE)
    * casmi v0.3.2 (http://casmi.github.io/)
    * logback
    * Java-WebSocket
    * ini4j
* MySQL
* NGSV Console (https://github.com/xcoo/ngsv-console)

# Setup configulation

Setup database settings.

    $ cp config/ngsv.ini.example config/ngsv.ini

    db.host     = [hostname]
    db.database = ngsv
    db.user     = [db_user]
    db.password = [db_password]

Setup viewer settings.

    $ cp config/viewer.ini.example config/viewer.ini

    [general]
    fps = 30.0
    window.width = 1024
    ...

# Usage

1. Execute NGSV.
2. Open NGSV Console on your web browser and move to viewer page (e.g. http://example.com/viewer).
3. Select sam files, bed files, a chromosome name, and region (and a gene name optionally). And click 'Select'.
4. Drag left or right to scroll the view.
5. With mouse wheel scrolling, you can change scale of view.
6. With mouseover, you can see detail information of elements.
7. 'Space + drag' to display 3-D view. 'r' to reset the view.

# Package

Build main project and package as executable file (.app/.exe).

## Mac OS X

    $ mvn -P production-mac -Dmaven.test.skip=true package

## Windows

    $ mvn -P production-win -Dmaven.test.skip=true package

 Copy native jar files to the exe directory.

     $ cp [jogl-all-2.0-rc11-natives-windows-i586.jar] [exe directory]
     $ cp [gluegen-all-2.0-rc11-natives-windows-i586.jar] [exe directory]

# License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
