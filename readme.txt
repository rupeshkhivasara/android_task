Android Task 
~~~~~~~~~~~~~~~~~~~~


This directory contains the full implementation of a basic application for
the Android platform, demonstrating the basic facilities that applications
will use.  You can run the application  directly from the "MainActivity"

The files contained here:


AndroidManifest.xml

This XML file describes to the Android platform what your application can do.
It is a required file, and is the mechanism you use to show your application
to the user (in the app launcher's list), it also include the permissions which requires.
in this task we are using INTERNET and ACCESS_NETWORK_STATE permissions.


src/*

Under this directory is the Java source for for your application.

ui/*
src/com.demo.kawatask/ui/MainActivity.java

This is the implementation of the "activity" feature described in
AndroidManifest.xml.  The path each class implementation is
{src/PACKAGE/CLASS.java}, where PACKAGE comes from the name in the <package>
tag and CLASS comes from the class in the <activity> tag.

Adapter/*
Model/*
utils/*
res/*

Under this directory are the resources for your application.


res/layout/skeleton_activity.xml

The res/layout/ directory contains XML files describing user interface
view hierarchies.  The activity_main.xml file here is used by
MainActivity.java to construct its UI.  The base name of each file
(all text before a '.' character) is taken as the resource name;
it must be lower-case.


res/drawable/

The res/drawable/ directory contains images and other things that can be
drawn to the screen.  These can be bitmaps (in .png or .jpeg format) or
special XML files describing more complex drawings.

res/values/colors.xml
res/values/strings.xml
res/values/styles.xml

These XML files describe additional resources included in the application.
They all use the same syntax; all of these resources could be defined in one
file, but we generally split them apart as shown here to keep things organized.