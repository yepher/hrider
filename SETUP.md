## Command Line Build

To build the project at the command line you need to have maven installed. When building with Maven you must specify a profile to use.

For instance

```
mvn clean install -P 0.94.1
```

# Running from Command Line

Once build you can run HRider with the following command:

```
> cd target
> java -jar h-rider-1.0.7.0.jar
```



## Eclipse Build

To build in eclipse you need the M2E plugin installed in Ecliplse. To setup and build the project do the following:

* Open Eclipse
* Select Menu->File->Import->Existing Maven Project
* Browser to HRider directory
* Select OK
* Once imported Right click on the HRider project and select the "maven" group
* Set "Active Maven Profile" to "0.94.1" (without the quotes)
* Close Properties


## Running in Eclispe

In Eclipse right click on _hrider.ui.forms.Window_ and select "Debug->Java Applicaiton"

