The goal of this project is to create an application that will work on android devices especially phones that will allow a person to take Ultimate Frisbee stats on the sidelines.

Importing the project:
Use your favorite git tool (eclipse has a plugin called "egit" that works well)  to get the files from this repository
The do New->Android Project: Select "Create project from existing source"

Since my plan is to send this app to ultimate players that are not nesicarily android developers I will post a few useful links below:
Getting started with android development in eclipse:
http://developer.android.com/sdk/installing.html
http://developer.android.com/resources/tutorials/hello-world.html

The uml editor I intend to use:
http://www.umlet.com/
I will attempt to leave current or fairly current copies of the uml diagram in PDF format in with the uml produced by UMLet, this is the only compiled binary I intend to have in the repository incase people do not wish to install UMLet.

Sometimes you will get an error with the emulator launching if you use one it will say something along the lines of:
[2011-07-10 07:11:06 - Emulator] invalid command-line parameter: Files\Android\android-sdk\tools/emulator-arm.exe.
[2011-07-10 07:11:07 - Emulator] Hint: use '@foo' to launch a virtual device named 'foo'.
[2011-07-10 07:11:07 - Emulator] please use -help for more information
This is due to a bug in the SDK refer to this:
http://stackoverflow.com/questions/6638713/android-emulator-is-not-starting-showing-invalid-command-line-parameter
which says "change the path to the sdk to have no spaces, one way to do this is to change the path to C:\PROGRA~2\Android\android-sdk from C:\Programme Files(x86)\Android\android-sdk"
this says how to change the path: 
http://androidforums.com/application-development/5270-change-path-sdk-eclipse.html

Helpful tips when writing code:
Declaring the same variable in a class and in a function and only allocating the one in the function will throw nullPointerExceptions when you try to use that variable in another function (I know its obvious but I'm dumb sometimes for like 3 hours)
Make sure you have the correct AndroidManifest.xml permissions. Weird errors will be thrown or not if you don't. 

Added //LONGTERMTODO tag in Window->Preferences->Java->Compiler->Task Tags, you should too