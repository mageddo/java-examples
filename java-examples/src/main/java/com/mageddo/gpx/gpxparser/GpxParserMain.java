package com.mageddo.gpx.gpxparser;

import me.himanshusoni.gpxparser.GPXParser;
import me.himanshusoni.gpxparser.extension.DummyExtensionHolder;
import me.himanshusoni.gpxparser.modal.Track;
import me.himanshusoni.gpxparser.modal.TrackSegment;
import me.himanshusoni.gpxparser.modal.Waypoint;

public class GpxParserMain {
  public static void main(String[] args) throws Exception {

    final var in = GpxParserMain.class.getResourceAsStream("/bike-ride.gpx");
    final var p = new GPXParser();
    final var gpx = p.parseGPX(in);

    for (Track track : gpx.getTracks()) {
      for (TrackSegment segment : track.getTrackSegments()) {

        for (Waypoint waypoint : segment.getWaypoints()) {

          final var ext = (DummyExtensionHolder) waypoint
              .getExtensionData()
              .get("Basic Extension Parser");

          System.out.printf(
              "way, time=%s, lat=%f, lon=%f, mag=%f, ele=%f",
              waypoint.getTime().toInstant(),
              waypoint.getLatitude(),
              waypoint.getLongitude(),
              waypoint.getMagneticVariation(),
              waypoint.getElevation()
          );

          final var childs = ext.getNodeList()
              .item(1)
              .getChildNodes();


          for (int i = 0; i < childs.getLength(); i++) {

            final var item = childs.item(i);
            final var nodeName = item.getNodeName();

            if (nodeName.equals("gpxtpx:hr")) {
              System.out.printf(", hr=%s", item.getTextContent());
            }
          }
          System.out.printf(", ext=%s%n", waypoint.getExtensionData());
        }
        System.out.printf("segment, ext data=%s%n", segment.getExtensionData());
        System.out.println();
      }
    }
  }
}
