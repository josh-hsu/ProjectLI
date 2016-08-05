package com.mumu.projectli;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * ElectricityRecordHandler
 *
 * This class handle electricity data and file
 */
public class ElectricityRecordHandler {
    private final static String TAG = "ProjectLI";
    private List<ElectricityRecordParser.Entry> mHistoryList;
    private Context mContext;
    private String mDataDirectory;

    public ElectricityRecordHandler(Context context) {
        mContext = context;
        mDataDirectory = context.getFilesDir().getAbsolutePath();
        init();
    }

    private void init() {
        InputStream userDataStream;
        String userDataPath = mDataDirectory + mContext.getString(R.string.electric_data_file_name);

        try {
            userDataStream = new FileInputStream(userDataPath);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "User file not found, create on from resource");
            userDataStream = copyDefaultRecordToUser(userDataPath);
        }

        try {
            mHistoryList = new ElectricityRecordParser().parse(userDataStream);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Parsing XML file failed. Fetching xml to developer.");
            return;
        }
        Log.d(TAG, "xml data:");
        for (ElectricityRecordParser.Entry entry: mHistoryList) {
            Log.d(TAG, "  " + entry.toString());
        }
    }

    private InputStream copyDefaultRecordToUser(String path) {
        InputStream in = mContext.getResources().openRawResource(R.raw.electricity_sample);
        OutputStream out = null;

        try {
            out = new FileOutputStream(new File(path));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    in = new FileInputStream(new File(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return in;
    }

    public void refreshFromFile() {
        init();
    }

    // Fetch content of list
    public List<ElectricityRecordParser.Entry> getHistoryList() {
        return mHistoryList;
    }

    public ElectricityRecordParser.Entry get(int idx) {
        if (idx < mHistoryList.size())
            return mHistoryList.get(idx);
        else
            return null;
    }

    public int getCount() {
        return mHistoryList.size();
    }

    public int getInverseIndex(int idx) {
        return getCount() - idx - 1;
    }

    public String getRecord(int idx) {
        if (idx < getCount())
            return get(getInverseIndex(idx)).record;
        else
            return null;
    }

    public String getDate(int idx) {
        if (idx < getCount())
            return get(getInverseIndex(idx)).date;
        else
            return null;
    }

    public String getDateFormatted(int idx) {
        String rawDate = getDate(idx);

        try {
            Date df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(rawDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(df);
            Calendar today = Calendar.getInstance();
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            DateFormat timeFormatter = new SimpleDateFormat("hh:mma");

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                return mContext.getString(R.string.electric_graphic_today) + " " + timeFormatter.format(df);
            } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
                return mContext.getString(R.string.electric_graphic_yesterday) + " " + timeFormatter.format(df);
            } else {
                return df.toString();
            }
        } catch (ParseException e) {
            Log.e(TAG, "Parsing date string failed");
            e.printStackTrace();
        }

        return "";
    }

    public String getSerial(int idx) {
        if (idx < getCount())
            return get(getInverseIndex(idx)).serial;
        else
            return null;
    }

    public String getNextSerial() {
        if (getCount() > 0) {
            return "" + (Integer.parseInt(get(getInverseIndex(0)).serial) + 1);
        } else {
            return "0";
        }
    }

    public int getIncrement(int idx) {
        if (idx < getCount() - 1)
            return Integer.parseInt(getRecord(idx)) - Integer.parseInt(getRecord(idx+1));
        else
            return -1;
    }

    // Record operation
    public boolean addRecord (ElectricityRecordParser.Entry record) throws Exception {
        String userDataPath = mDataDirectory + mContext.getString(R.string.electric_data_file_name);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(userDataPath));
        Element root = document.getDocumentElement();

        // add record to runtime immediately
        mHistoryList.add(record);

        // add record to xml
        Element subroot = document.createElement("entry");
        Element serial = document.createElement("serial");
        serial.appendChild(document.createTextNode(record.serial));
        subroot.appendChild(serial);
        Element date = document.createElement("date");
        date.appendChild(document.createTextNode(record.date));
        subroot.appendChild(date);
        Element rec = document.createElement("record");
        rec.appendChild(document.createTextNode(record.record));
        subroot.appendChild(rec);

        root.appendChild(subroot);

        // save it
        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(userDataPath);
        transformer.transform(source, result);

        return true;
    }
}
