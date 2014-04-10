#JSON Benchmarks

Using JMH which is what open JDK uses.

#Environment

OSX MacBook Pro, JDK 1.7, 16GB RAM and 512GB SSD drive.

#Quick FAQ

##Why don't you test GSON and JSON smart?

Jackson is faster than GSON and JSON smart.
We have tests for all three.

##Why don't you test Jackson AST?

I do.
I see no real difference between readTree and readValue.
I left out AST because it is redundant.


##Who wrote the benchmarks?

Originally they were written by Stephane Landelle.
Later they were added to by Andrey Bloschetsov for Groovy serialization.
I've been using this repo as a dumping ground to bench mark all sorts of Boon stuff.
I need to create another repo at some point.


##Relationship to Groovy JSON parser for 2.3?

Andrey Bloschetsov and I with the help of the Groovy lead developers
took the Boon classes and forked them into Groovy 2.3.

We did this mainly for parsing, but we also improved the JSON serialization speed.

Boon and Groovy parsing are unsurprisingly very comparable.

Groovy JSON parsing is a bit faster in a few use cases, and Boon parser is a bit faster in a few uses cases,
but mostly they are neck in neck.

Boon JSON serialization is still quite a bit faster than Groovy.
This will change in Groovy 2.4 or 2.5.
The new Groovy JSON parser based on Boon is 20x faster than the Groovy 2.2 parser.

#JSON

Most JSON is taken from json.org sample files.
The idea was to be fair.


#InputStream

I ran this with JMH as follows:

```
 java -jar target/microbenchmarks.jar ... -wi 2 -i 3 -f 2 -t 16
```


Two warm-ups. Two forks. 16 threads. Three iterations.
Jackson and Boon are completely warmed up after the first iteration.
Running it longer does not help.

Boon.

```java

    private Object parse(InputStream inputStream) throws Exception {
        return parser.parse (inputStream);
    }

```

Jackson.
```java
    private Object parse(InputStream inputStream) throws Exception {

            return JACKSON_MAPPER.readValue(inputStream, Map.class);
    }

```



## InputStream actionLabel.json 872 bytes


```
Benchmark                                                    Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.inputStream.MainBoonBenchmark.actionLabel             thrpt  16         6    1   866021.511   152818.015    ops/s
i.g.j.inputStream.MainJacksonObjectBenchmark.actionLabel    thrpt  16         6    1   662051.786    70960.055    ops/s
```

Boon is faster.

## InputStream citm_catalog.json 1.7 MB (1,700,000 bytes)


```
i.g.j.inputStream.MainBoonBenchmark.citmCatalog             thrpt  16         6    1      997.894      106.665    ops/s
i.g.j.inputStream.MainJacksonObjectBenchmark.citmCatalog    thrpt  16         6    1      464.647       82.287    ops/s
```

Boon is a lot faster.

## InputStream medium.json 2 KB (2,000 bytes)
```
i.g.j.inputStream.MainBoonBenchmark.medium                  thrpt  16         6    1   729649.386    32040.721    ops/s
i.g.j.inputStream.MainJacksonObjectBenchmark.medium         thrpt  16         6    1   419224.947    21098.385    ops/s
```

Boon wins.


## InputStream menu.json 256 bytes
```
i.g.j.inputStream.MainJacksonObjectBenchmark.menu           thrpt  16         6    1  1985725.125   181813.950    ops/s
i.g.j.inputStream.MainBoonBenchmark.menu                    thrpt  16         6    1  1505494.119    24945.816    ops/s
```

Jackson wins. (Can't win them all.)
For this small of a payload you might need to tweak the default buffer sizes.

## InputStream sgml.json 705 bytes

```
i.g.j.inputStream.MainBoonBenchmark.sgml                    thrpt  16         6    1  1350396.608    31969.601    ops/s
i.g.j.inputStream.MainJacksonObjectBenchmark.sgml           thrpt  16         6    1  1209915.253   122783.831    ops/s
```

Boon barely wins.

## InputStream webxml.json 4K bytes

```
i.g.j.inputStream.MainBoonBenchmark.webxml                  thrpt  16         6    1   431055.661    31544.139    ops/s
i.g.j.inputStream.MainJacksonObjectBenchmark.webxml         thrpt  16         6    1   189635.644   104334.060    ops/s
```

Boon wins by a large margin.


## InputStream widget.json 761 bytes
```
i.g.j.inputStream.MainBoonBenchmark.widget                  thrpt  16         6    1  1066325.400   108550.520    ops/s
i.g.j.inputStream.MainJacksonObjectBenchmark.widget         thrpt  16         6    1   699439.433   130830.543    ops/s
```

Boon wins by a large margin.


#Caveat
Boon is a parser optimized for REST and websocket services.
It is optimized to work in a reactive style environment like Akka, Vert.x, etc.
It would work well with MessageDrivenBeans (EJB or Spring).
For some use cases it runs stateless faster than Jackson runs with shared buffers.
Benchmark first.
It would take some extra effort to get superior results in a Servlet environment (I can do it, but...).
Jackson is mature and solid. Don't assume ripping out Boon for Jackson will buy you anything.
Jackson has tooling that makes it work really well in a Java EE environment.




#File system read

Both Boon and Jackson have the ability to read straight from the file system.
It seems Jackson needed more warm-ups so I added them.
I don't think non-warmed up code proves anything.
Both Jackson and Boon were warmed up after three warm-ups.

Boon is much faster than Jackson with Reader, byte[] (2x to 4x), and char[] and Strings.

Boon is 3x to 5x faster than Jackson at parsing Strings and char[].


```
 java -jar target/microbenchmarks.jar ".*file.*"  -wi 3 -i 3 -f 2 -t 16
```

Jackson File.

```java

    private Object parse(String fileName) throws Exception {
            return JACKSON_MAPPER.readTree ( new File (fileName) );
    }

```

Boon File.

```java

    private Object parse(String fileName) throws Exception {
        return parser.parseFile ( Map.class, fileName);
    }

```

# Overall

Boon has the hardest time with InputStream.
Boon and Jackson are close at handling input stream if the JSON stream is small.
Once the JSON stream gets over 1K, Boon is the clear winner.





## File actionLabel.json 872 bytes


```
i.g.j.f.BoonBenchMark.actionLabel          thrpt  16         6    1   253893.017    14314.289    ops/s
i.g.j.f.JacksonASTBenchmark.actionLabel    thrpt  16         6    1   231613.819    25364.317    ops/s
```

Call it a tie. Boon barely wins.

## File citm_catalog.json 1.7 MB (1,700,000 bytes)


```
i.g.j.f.BoonBenchMark.citmCatalog          thrpt  16         6    1      862.778      106.362    ops/s
i.g.j.f.JacksonASTBenchmark.citmCatalog    thrpt  16         6    1      451.617       59.926    ops/s
```

Boon wins by a wide margin.


## File medium.json 2 KB (2,000 bytes)
```
Benchmark                                   Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.f.BoonBenchMark.medium               thrpt  16         6    1   249553.100    19159.077    ops/s
i.g.j.f.JacksonASTBenchmark.medium         thrpt  16         6    1   197056.072    21201.261    ops/s
```

Boon wins.


## File menu.json 256 bytes

```
Benchmark                                   Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.f.BoonBenchMark.menu                 thrpt  16         6    1   271678.375    24672.358    ops/s
i.g.j.f.JacksonASTBenchmark.menu           thrpt  16         6    1   284756.122    88111.175    ops/s
```

Call it a tie. Jackson barely wins.


## File sgml.json 705 bytes

```
Benchmark                                   Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.f.BoonBenchMark.sgml                 thrpt  16         6    1   258278.919     9886.144    ops/s
i.g.j.f.JacksonASTBenchmark.sgml           thrpt  16         6    1   253126.067    20414.849    ops/s
```

Call it a tie.
Boon barely wins.

## File webxml.json 4K bytes

```
Benchmark                                   Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.f.BoonBenchMark.webxml               thrpt  16         6    1   208778.647    15219.752    ops/s
i.g.j.f.JacksonASTBenchmark.webxml         thrpt  16         6    1   127184.025    17270.754    ops/s
```

Boon wins by a large margin.


## File widget.json 761 bytes

```
Benchmark                                   Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.f.BoonBenchMark.widget               thrpt  16         6    1   268296.542    21856.333    ops/s
i.g.j.f.JacksonASTBenchmark.widget         thrpt  16         6    1   222440.689    39514.739    ops/s
```

Boon wins.


#Reader

```
Benchmark                                          Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.r.MainBoonBenchmark.actionLabel             thrpt  16         6    1  1482160.136   127335.188    ops/s
i.g.j.r.MainJacksonObjectBenchmark.actionLabel    thrpt  16         6    1   528522.919    70537.978    ops/s

Boon 3x faster

i.g.j.r.MainBoonBenchmark.citmCatalog             thrpt  16         6    1     1102.425      272.243    ops/s
i.g.j.r.MainJacksonObjectBenchmark.citmCatalog    thrpt  16         6    1      396.981       28.491    ops/s

Boon over 2x faster

i.g.j.r.MainBoonBenchmark.medium                  thrpt  16         6    1  1164408.836    71138.499    ops/s
i.g.j.r.MainJacksonObjectBenchmark.medium         thrpt  16         6    1   365940.803    15780.746    ops/s

Boon 2.7x faster.

i.g.j.r.MainBoonBenchmark.menu                    thrpt  16         6    1  4730577.939   206541.736    ops/s
i.g.j.r.MainJacksonObjectBenchmark.menu           thrpt  16         6    1  1581462.192   289868.322    ops/s

Boon over 2x faster.

i.g.j.r.MainBoonBenchmark.sgml                    thrpt  16         6    1  4813803.211   157129.919    ops/s
i.g.j.r.MainJacksonObjectBenchmark.sgml           thrpt  16         6    1  1643288.822    80243.617    ops/s

Boon over 2x faster.

i.g.j.r.MainBoonBenchmark.webxml                  thrpt  16         6    1   646994.644   101870.656    ops/s
i.g.j.r.MainJacksonObjectBenchmark.webxml         thrpt  16         6    1   203544.281    15564.338    ops/s

Boon over 3x faster.

i.g.j.r.MainBoonBenchmark.widget                  thrpt  16         6    1  1922759.581   360131.835    ops/s
i.g.j.r.MainJacksonObjectBenchmark.widget         thrpt  16         6    1   639264.142    36270.570    ops/s

Boon almost 3x faster.

```


#Parse From byte[] array

```
Benchmark                                          Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.b.MainBoonBenchmark.actionLabel             thrpt  16         6    1  1221958.008    30506.924    ops/s
i.g.j.b.MainJacksonObjectBenchmark.actionLabel    thrpt  16         6    1   636254.022    33273.771    ops/s

Boon wins by 2x

i.g.j.b.MainBoonBenchmark.citmCatalog             thrpt  16         6    1     1000.572       79.712    ops/s
i.g.j.b.MainJacksonObjectBenchmark.citmCatalog    thrpt  16         6    1      537.311       61.683    ops/s


Boon wins by almost 2x

i.g.j.b.MainBoonBenchmark.medium                  thrpt  16         6    1   854426.764    84604.024    ops/s
i.g.j.b.MainJacksonObjectBenchmark.medium         thrpt  16         6    1   436113.169    34620.702    ops/s

Boon wins by almost 2x

i.g.j.b.MainBoonBenchmark.menu                    thrpt  16         6    1  3740629.358   570942.364    ops/s
i.g.j.b.MainJacksonObjectBenchmark.menu           thrpt  16         6    1  1842253.614    50144.188    ops/s

Boon wins by 2x.

i.g.j.b.MainBoonBenchmark.sgml                    thrpt  16         6    1  2377758.261   162251.992    ops/s
i.g.j.b.MainJacksonObjectBenchmark.sgml           thrpt  16         6    1  1190724.547    60984.294    ops/s

Boon wins by over 2x

i.g.j.b.MainBoonBenchmark.small                   thrpt  16         6    1 14608691.917  4637278.405    ops/s
i.g.j.b.MainJacksonObjectBenchmark.small          thrpt  16         6    1  3843711.836   237716.513    ops/s

Boon wins by 4x.

i.g.j.b.MainBoonBenchmark.webxml                  thrpt  16         6    1   503674.722    30799.444    ops/s
i.g.j.b.MainJacksonObjectBenchmark.webxml         thrpt  16         6    1   225047.361    31226.580    ops/s

Boon wins by over 2x.

i.g.j.b.MainBoonBenchmark.widget                  thrpt  16         6    1  1465630.533    38963.901    ops/s
i.g.j.b.MainJacksonObjectBenchmark.widget         thrpt  16         6    1   718188.167    76972.830    ops/s

Boon wins by 2x.

```



# Parse From String array


## String

```
Benchmark                                          Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.s.MainBoonBenchmark.actionLabel             thrpt  16         6    1  1431392.169   193758.838    ops/s
i.g.j.s.MainJacksonObjectBenchmark.actionLabel    thrpt  16         6    1   564775.347    17677.459    ops/s

Boon is 3x faster than Jackson


i.g.j.s.MainBoonBenchmark.citmCatalog             thrpt  16         6    1     1531.678      304.146    ops/s
i.g.j.s.MainJacksonObjectBenchmark.citmCatalog    thrpt  16         6    1      423.206       54.482    ops/s


Boon is over 3x faster than Jackson

i.g.j.s.MainBoonBenchmark.medium                  thrpt  16         6    1  1162388.483    58546.058    ops/s
i.g.j.s.MainJacksonObjectBenchmark.medium         thrpt  16         6    1   366282.108    20279.569    ops/s


Boon is 3.5x faster than Jackson

i.g.j.s.MainBoonBenchmark.menu                    thrpt  16         6    1  4606554.667   390199.354    ops/s
i.g.j.s.MainJacksonObjectBenchmark.menu           thrpt  16         6    1  1591678.644    99655.195    ops/s

i.g.j.s.MainBoonBenchmark.sgml                    thrpt  16         6    1  2930363.631   928801.932    ops/s
i.g.j.s.MainJacksonObjectBenchmark.sgml           thrpt  16         6    1  1007490.344   100771.781    ops/s


Boon is almost 3x faster than Jackson

i.g.j.s.MainBoonBenchmark.webxml                  thrpt  16         6    1   703755.139    16904.302    ops/s
i.g.j.s.MainJacksonObjectBenchmark.webxml         thrpt  16         6    1   197618.389     9224.057    ops/s


Boon is almost 4x faster than Jackson

i.g.j.s.MainBoonBenchmark.widget                  thrpt  16         6    1  2039753.711   164417.077    ops/s
i.g.j.s.MainJacksonObjectBenchmark.widget         thrpt  16         6    1   640933.911    30993.768    ops/s


Boon is almost 4x faster than Jackson


i.g.j.s.MainBoonBenchmark.small                   thrpt  16         6    1 26063946.758  1320123.707    ops/s
i.g.j.s.MainJacksonObjectBenchmark.small          thrpt  16         6    1  4328478.361   369396.951    ops/s


Boon is 5x faster than Jackson


```

#Object Serialization

Jackson and Boon can do Object serialization.
Boon uses this in SlumberDB to provide a fast key/value JSON store for Java using LevelDB, RocksDB, and MySQL.

```
Benchmark                                        Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.s.MainBoonSerializer.roundTriper          thrpt  16         6    1   327384.050     7562.980    ops/s
i.g.j.s.MainJacksonSerializer.roundTriper       thrpt  16         6    1   210291.358    15730.171    ops/s

Boon 30% faster.

i.g.j.s.MainBoonSerializer.serializeSmall       thrpt  16         6    1   913616.875    89584.750    ops/s
i.g.j.s.MainJacksonSerializer.serializeSmall    thrpt  16         6    1   703988.967   190145.965    ops/s

Boon 20% faster.

```


#Weird Stuff serialization


```
Benchmark                                                            Mode Thr     Count  Sec         Mean   Mean error    Units
i.g.j.serializerTests.Test.complexTestJackson                       thrpt  16         6    1    69923.967     2382.158    ops/s
i.g.j.serializerTests.Test.complextTestBoon                         thrpt  16         6    1    65746.303     7178.469    ops/s

Faster.

i.g.j.serializerTests.Test.mediumTestBoon                           thrpt  16         6    1  1323458.047    55113.844    ops/s
i.g.j.serializerTests.Test.mediumTestJackson                        thrpt  16         6    1   848145.458   109868.000    ops/s

Boon almost 2x faster.

i.g.j.serializerTests.Test.simpleTestBoon                           thrpt  16         6    1  5762613.228   119177.393    ops/s
i.g.j.serializerTests.Test.simpleTestJackson                        thrpt  16         6    1  2611158.339   411964.607    ops/s

Boon 2x faster.

```

