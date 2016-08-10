package com.mumu.projectli.records;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 2016/7/5.
 */
public class ElectricityRecordParser {
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readUserData(parser);
        } finally {
            in.close();
        }
    }

    private List readUserData(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "user");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    public static class Entry {
        public final String serial;
        public final String date;
        public final String record;

        public Entry(String s, String d, String r) {
            this.serial = s;
            this.date = d;
            this.record = r;
        }

        public String toString() {
            return "Serial=" + serial + ", " +
                    "Date=" + date + ", " +
                    "Record=" + record;
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String serial = null;
        String date = null;
        String record = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("serial")) {
                serial = readSerial(parser);
            } else if (name.equals("date")) {
                date = readDate(parser);
            } else if (name.equals("record")) {
                record = readRecord(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(serial, date, record);
    }

    private String readSerial(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "serial");
        String serial = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "serial");
        return serial;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "date");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "date");
        return date;
    }

    // Processes summary tags in the feed.
    private String readRecord(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "record");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "record");
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
