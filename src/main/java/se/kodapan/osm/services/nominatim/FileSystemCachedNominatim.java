package se.kodapan.osm.services.nominatim;

import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author kalle
 * @since 2013-07-28 19:59
 */
public class FileSystemCachedNominatim extends AbstractCachedNominatim {

  private File path;

  public FileSystemCachedNominatim(File path) {
    this.path = path;
  }

  @Override
  public void open() throws Exception {
    super.open();
    if (!path.exists() && !path.mkdirs()) {
      throw new IOException("Could not mkdirs " + path.getAbsolutePath());
    }
  }

  @Override
  public String getCachedResponse(String overpassQuery) throws Exception {
    String filename = getFileName(overpassQuery);
    File file = new File(path, filename);
    if (!file.exists()) {
      return null;
    }
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
    Record record = (Record) ois.readObject();
    ois.close();

    return record.getNominatimResponse();
  }

  @Override
  public void setCachedResponse(String overpassQuery, String overpassResponse) throws Exception {

    String filename = getFileName(overpassQuery);
    File file = new File(path, filename);

    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
    oos.writeObject(new Record(overpassQuery, overpassResponse, System.currentTimeMillis()));
    oos.close();


  }

  private String getFileName(String overpassQuery) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    return Hex.encodeHexString(md.digest(overpassQuery.getBytes()));
  }

  public static class Record implements Serializable {
    private static final long serialVersionUID = 1l;
    private String url;
    private String nominatimResponse;
    private long timestamp;


    public Record() {
    }

    public Record(String url, String nominatimResponse, long timestamp) {
      this.url = url;
      this.nominatimResponse = nominatimResponse;
      this.timestamp = timestamp;
    }

    public String getNominatimResponse() {
      return nominatimResponse;
    }

    public void setNominatimResponse(String nominatimResponse) {
      this.nominatimResponse = nominatimResponse;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }
  }


}
