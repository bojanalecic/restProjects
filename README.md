restProjects
============
About the project


The idea of this project is to create an application for extracting, structuring and integrating datas about software projects from certain web pages. 
Initally, this application extracted datas from the website Freecode, converted datas to appropriate RDF representation(according to DOAP1 vocabulary).
It also allows user to search and display information about software projects. Now, it is expanded with datas from SourceForge web site. 
All the datas about software project, name, programming languages, operating systems, licenses, maintainer  are extracted and stored in database.
 Metadata is inserted in site's webpages in a structured format using Microdata standard, specifically using Schema.org vocabulary. After the metadata is extracted, it is transformed to RDF format and stored into RDF repository. 
 Access to the extracted data is enabled through RESTful service. 
 
Application workflow consists of the following phases:

-A web crawler parses projects from Sourceforge website and extracts project metadata
-Extracted data is transformed into RDF triplets based on Schema.org vocabulary
-Data is persisted into an RDF repository
-Access to the data is enabled through RESTful services
-Search of projects is enabled by ajax
-Webpage with all software projects is enriched with microdata tags

Domain model

Webpages of software projects from the Freecode and SourceForge websites are analyzed in order to determine which classes and properties form the Schema.org vocabulary are supported. Based on that analysis, 
domain model is created and it is depicted in Picture 1.



Class Project contains basic information about a project. Those are: name, decription, download-page, homepage, seeAlso, programing-languages, operating-systems, license. It has reference to its maintainer (class Person), its category (class Category),  and its release (class Version).

Class Person contains maintainers's name and URI seeAlso.

Class Category contains  name and URI seeAlso.

Class Version contains basic information of project release such as name, date when it is created, width, revision and description.


The solution


Application collects metadata about Software projects from the webpages Freecode and SourceForge. The data is extracted by the crawler and is used to create domain objects of the application that are persisted into the RDF repository. 
The application allows access to that data via RESTful service.

Application contains several REST services.

/projects - returns all projects in JSON format
/licenses - returns all licenses in JSON format
/oss - returns all operating systems in JSON format
/languages - returns all programming languages in JSON format
/tags - returns all tags in JSON format
/projsearch - returns filtered projects by specified criteria



Technical realisation

This application is written in programming language Java.

Jsoup library is used for analyzing and collecting data from the web pages. It provides a very convenient API for extracting and manipulating data, using the best of DOM, CSS, and jquery-like methods.

This application also uses Jenabean library for mapping Java objects into RDF triplets using annotations. Jenabean provides explicit binding between an object property and a particular RDF property.

Jena TDB library is used for data storage in the RDF repository. TDB is a component of Jena for RDF storage and query. It support the full range of Jena APIs.

Implementation of the RESTful web service is supported by Jersey framework. Jersey is the open source JAX-RS Reference Implementation for building RESTful Web services. It uses annotations which define type of the HTTP requests (GET, POST ...) and also the path to the requested resource. Datas are extracted from database using SPARQL queries. 

Those queries returns the resultset, and from each result, one Project is created. Then,  list of Project entities is converted to JSON file that is parsed to html representation.

Webpage for project search is enriched with appropriate microdata tags, according to http://schema.org/SoftwareApplication specification. Every software project is 

denoted as itemscope, and its itemtype is SoftwareApplication. Author - maintainer is itemscope which type is Person. Now, google will recognize these datas as really

Software Applications.


Acknowledgements

This application was initially developed by former student at Faculty of Organizational Sciences, Boban Cirkovic.

It has been developed as a part of the project assignment for the subject Intelligent Systems at the Faculty of Organization Sciences, University of Belgrade, Serbia.



Licence



This software is licensed under the MIT License.

The MIT License (MIT)

Copyright (c) 2013 Bojana Lecic - bojanalcc@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.