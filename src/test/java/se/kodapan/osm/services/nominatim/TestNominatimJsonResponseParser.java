package se.kodapan.osm.services.nominatim;

import se.kodapan.osm.OsmCommonTest;
import se.kodapan.osm.services.nominatim.NominatimJsonResponseParser.Result;
import se.kodapan.osm.services.overpass.Overpass;

import java.util.List;

/**
 * @author kalle
 * @since 2013-07-27 21:15
 */
public class TestNominatimJsonResponseParser extends OsmCommonTest {

  public void test() throws Exception {

    NominatimQueryBuilder queryBuilder = new NominatimQueryBuilder()
            .setQuery("Halmstad, Sverige")
            .setLimit(10)
            .setFormat("json")
            .addCountryCode("se");

    Nominatim nominatim = new Nominatim();
    setUserAgent(nominatim);
    NominatimJsonResponseParser parser = new NominatimJsonResponseParser();
    List<Result> results = parser.parse(nominatim.search(queryBuilder.build()));

    assertTrue(results.size() > 2);
    assertEquals(parser.getRoot().gatherAllOsmObjects().size(), results.size());

    results = parser.parse(nominatim.search(queryBuilder.setLimit(1).build()));
    assertEquals(1, results.size());

    Overpass overpass = new Overpass();
    setUserAgent(overpass);
    overpass.loadAllObjects(parser.getRoot());


    System.currentTimeMillis();

  }

}
