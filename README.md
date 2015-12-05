**Pull requests (e.g. support for more API endpoints, bugfixes) against dev are welcome!**

tmdb-java-2
============

A Java wrapper around the [TMDb v3 API][1] using [Retrofit 2][2].

Usage
-----
![Maven Central version](https://img.shields.io/maven-central/v/no.askeland/tmdb-java-2.svg?style=flat-square)

Add the following dependency to your Gradle project:

```groovy
compile 'no.askeland:tmdb-java-2:2.0.0'
```

or your Maven project:

```xml
<dependency>
    <groupId>no.askeland</groupId>
    <artifactId>tmdb-java-2</artifactId>
    <version>2.0.0</version>
</dependency>
```


Example
-------

```java
// Create an instance of the service you wish to use
// you can keep this around
Tmdb tmdb = new Tmdb();
tmdb.setApiKey("yourapikey");
MovieService movieService = tmdb.movieService();
//
// Call any of the available endpoints
Movie movie = movieService.summary(550).execute().body();
Trailers trailers = movieService.trailers(550).execute().body();
```

See test cases in `src/test/` for more examples.

Related projects
----------------

[tmdb-java](https://github.com/UweTrottmann/tmdb-java) - Original project, using Retrofit 1.9
[tmdb-rx-java](https://github.com/migueljteixeira/tmdb-rx-java) - Retrofit 1.9 with RxAndroid support

License
-------

    Copyright 2013-2015 Uwe Trottmann

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




 [1]: http://docs.themoviedb.apiary.io/
 [2]: https://github.com/square/retrofit
 [3]: https://github.com/UweTrottmann/tmdb-java/releases
