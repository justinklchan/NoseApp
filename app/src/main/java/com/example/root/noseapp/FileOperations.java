package com.example.root.noseapp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.ArrayList;

public class FileOperations {

    public static void writetofile(short[] buff,String filename, String dirname)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+dirname;

            Log.e("hello4",dir+File.separator+filename);

            File file = new File(dir+File.separator+filename);
            if(!file.exists())
            {
                file.createNewFile();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for(int i=0;i < buff.length;i++ )
            {
                buf.append(""+buff[i]);
                buf.newLine();
                //	buf.flush();
            }
            buf.flush();
            buf.close();
        }catch(Exception e)
        {
            Log.e("hello1",e.getMessage());
        }
    }

    public static void writetofile(ArrayList<Float> q1,ArrayList<Float> q2,ArrayList<Float> q3,
                                   ArrayList<Float> q4,ArrayList<Float> q5,
                                   String filename, String dirname)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+dirname;

            Log.e("hello4",dir+File.separator+filename);

            File file = new File(dir+File.separator+filename);
            if(!file.exists())
            {
                file.createNewFile();
            }

            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for(int i=0;i < q1.size();i++ )
            {
                buf.append(q1.get(i)+","+q2.get(i)+","+q3.get(i)+","+q4.get(i)+","+q5.get(i)+",");
                buf.newLine();
            }

            buf.flush();
            buf.close();
        }catch(Exception e)
        {
            Log.e("hello1",e.getMessage());
        }
    }

    public static void writetofile(double[] buff,String filename, String dirname)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+dirname;

            Log.e("hello5",dir+File.separator+filename);

            File file = new File(dir+File.separator+filename);
            if(!file.exists())
            {
                file.createNewFile();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for(int i=0;i < buff.length;i++ )
            {
                buf.append(""+buff[i]);
                buf.newLine();
                //	buf.flush();
            }
            buf.flush();
            buf.close();
        }catch(Exception e)
        {

        }

    }

    public static void deleteFile(String filename, String dirname)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+dirname;

            Log.e("hello6",dir+File.separator+filename);

            File file = new File(dir+File.separator+filename+".txt");

            file.delete();
        }catch(Exception e)
        {

        }
    }

    public static void writetofile(double[] buff,String filename)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir+File.separator+filename);
            if(!file.exists())
            {
                file.createNewFile();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file,false));
            for(int i=0;i < buff.length;i++ )
            {
                buf.append(""+buff[i]);
                buf.newLine();
                //	buf.flush();
            }
            buf.flush();
            buf.close();
        }catch(Exception e) {
        }
    }

    public static void appendtofile(double[] buff,String filename)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir+File.separator+filename);
            if(!file.exists())
            {
                file.createNewFile();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file,true));
            for(int i=0;i < buff.length;i++ )
            {
                buf.append(""+buff[i]);
                buf.newLine();
                //	buf.flush();
            }
            buf.flush();
            buf.close();
        }catch(Exception e) {
        }
    }

    public static void writetofile(String filename)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir+File.separator+filename);
            if(!file.exists())
            {
                file.createNewFile();
            }
        }catch(Exception e)
        {

        }
    }

    public static short[] readfromfile(String filename)
    {
        short [] buffer = new short[20*44100];
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir+File.separator+filename);

            BufferedReader buf = new BufferedReader(new FileReader(file));
            for(int i=0;i<buffer.length;i++)
            {
                buffer[i] = (short)Double.parseDouble(buf.readLine());
            }
            buf.close();
        }catch(Exception e)
        {

        }
        return buffer;
    }

    public static short[] readfromfile1(String filename)
    {
        short [] buffer = new short[20*44100];
        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir+File.separator+filename);

            BufferedReader buf = new BufferedReader(new FileReader(file));
            for(int i=0;i<buffer.length;i++)
            {
                buffer[i] = (short)Double.parseDouble(buf.readLine());
            }
            buf.close();
        }catch(Exception e)
        {

        }
        return buffer;
    }

    public static LinkedList<String> readfromfile2(String filename)
    {
        LinkedList<String> ll = new LinkedList<String>();

        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/data";
            File file = new File(dir+File.separator+filename);

            BufferedReader buf = new BufferedReader(new FileReader(file));

            String line;
            while((line=buf.readLine())!=null && line.length()!=0) {
                if (line.contains(",")) {
                    ll.add(line);
                }
            }

            buf.close();
        }catch(Exception e)
        {
            Log.e("test",e.getMessage());
        }
        return ll;
    }

    public static LinkedList<String> readfromfile3(String filename)
    {
        LinkedList<String> ll = new LinkedList<String>();

        try
        {
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/data";
            File file = new File(dir+File.separator+filename);

            BufferedReader buf = new BufferedReader(new FileReader(file));

            String line;
            while((line=buf.readLine())!=null && line.length()!=0) {
                ll.add(line);
            }

            buf.close();
        }catch(Exception e)
        {
            Log.e("test",e.getMessage());
        }
        return ll;
    }
}
