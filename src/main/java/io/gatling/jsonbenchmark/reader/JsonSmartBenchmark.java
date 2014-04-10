
package io.gatling.jsonbenchmark.reader;

import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.gatling.jsonbenchmark.bytes.Buffers;
import net.minidev.json.parser.JSONParser;

import org.boon.IO;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.logic.BlackHole;

public class JsonSmartBenchmark {

    private Object parse(Reader reader) throws Exception {

            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse( reader);


    }


    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void actionLabel(BlackHole bh) throws Exception {
        bh.consume(parse(new StringReader(Buffers.STR_ACTION_LABEL_BYTES)));
    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void citmCatalog(BlackHole bh) throws Exception {
        bh.consume(parse(new StringReader(Buffers.STR_CITM_CATALOG_BYTES)));

    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void medium(BlackHole bh) throws Exception {

        bh.consume(parse(new StringReader(Buffers.STR_MEDIUM_BYTES)));
    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void menu(BlackHole bh) throws Exception {
        bh.consume(parse(new StringReader(Buffers.STR_MENU_BYTES)));

    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void sgml(BlackHole bh) throws Exception {
        bh.consume(parse(new StringReader(Buffers.STR_MENU_BYTES)));
    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void webxml(BlackHole bh) throws Exception {
        bh.consume(parse(new StringReader(Buffers.STR_WEBXML_BYTES)));

    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void widget(BlackHole bh) throws Exception {
        bh.consume(parse(new StringReader(Buffers.STR_WIDGET_BYTES)));

    }

}
