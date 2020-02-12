# EURECOM Semester project autumn 2019

The goal was to analyse anomalies in BGP routing. This repository contains all the code used to parse data from RIPE.

<b>The repository is structured as follows:</b><p>
* parser - Java application for parsing MRT to csv, requires libbgpdump
* postgres - Docker compose image for Postgres database
* ripe-download-scripts - download scripts for MRT images from RIPE RIS
* ripe-api-python - Python scripts for populating selection table with AS countries from RIPE API
* parse-scripts - Varios scripts and used database funtions
* presentation - Presentation slides and report