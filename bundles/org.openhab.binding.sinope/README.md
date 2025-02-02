# Sinopé Binding

The integration happens through the Sinopé (GT150) bridge, which acts as an IP gateway to the Sinopé devices on the 916 Mhz ISM band.

This binding supports multiple gateways with multiple devices.

## Supported Things

The Sinopé GT125 bridge is required as a "bridge" for accessing any other Sinopé devices.

Right now, the following devices are supported:
- Thermostats (3000W and 4000W) (TH1120RF) 
- Dimmers and Switches (DM2500RF and SW2500RF). Note: Switches are handled as a dimmer that can have only two intensities: 0 (OFF) or 100 (ON).

## Discovery

The Sinopé Gateway (bridge) discovery is not supported for now. 
It will be added in future release.
The Sinopé devices discovery is implemented. 

## Prerequisites

### Bridge or the Sinopé Gateway 

First, you will need to get your API key from your Sinopé gateway.

Grab the latest release of the [sinope-core library](<https://github.com/chaton78/sinope-core/releases>)

On Windows, you can run the SinopeProtocol.exe (in the zip release). 
The gateway parameter is written on the back of the SinopéGateway (example, 002f-c2c2-dd88-aaaa). 
The addr parameter is the IP given to your gateway.

```
SinopeProtocol.exe -addr [YOUR_GATEWAY_IP_OR_HOSTNAME]  -gateway [YOUR_GATEWAY_ID] -login
Getting API Key  - PRESS WEB Button
Your api Key is: 0x12 0x57 0x55 0xD5 0xCD 0x4A 0xD5 0x33
```

 On other operating systems, using only a JVM, you can invoke directly the java command from the latest release of the [sinope-core library](<https://github.com/chaton78/sinope-core/releases>):

```
java -jar core-0.0.3-shaded.jar -addr [YOUR_GATEWAY_IP_OR_HOSTNAME]   -gateway [YOUR_GATEWAY_ID] -login
Getting API Key  - PRESS WEB Button
Your api Key is: 0x12 0x57 0x55 0xD5 0xCD 0x4A 0xD5 0x33
```

### Thing Discovery

You can use the same procedure to discover each device you want to use. 
You will need to provide the api key from the previous step. 
If you use spaces, please, use double quotes to pass the api key (i.e. "0x12 0x57 0x55 0xD5 0xCD 0x4A 0xD5 0x33") 

Use the device procedure to discover it. 
For a thermostat, you need to push both buttons. 
The application will loop forever, press ctrl-c to exit.

```
SinopeProtocol.exe -addr [YOUR_GATEWAY_IP_OR_HOSTNAME]  -gateway [YOUR_GATEWAY_ID] -api "[YOUR_API_KEY]" -discover

It is now time to push both buttons on your device!
Press crtl-c to exit!
Your device id is: 0x00 0x00 0x35 0x86
It is now time to push both buttons on your device!
Press crtl-c to exit!
```

On other operating systems, using only a JVM, you can invoke directly the java command:

```
java -jar core-0.0.3-shaded.jar -addr [YOUR_GATEWAY_IP_OR_HOSTNAME]  -gateway [YOUR_GATEWAY_ID] -api "[YOUR_API_KEY]" -discover

It is now time to push both buttons on your device!
Press crtl-c to exit!
Your device id is: 0x00 0x00 0x35 0x86
It is now time to push both buttons on your device!
Press crtl-c to exit!
```

## Thing Configuration

The Sinopé bridge requires the address, the gateway id and the API key in order for the binding to know where and how to access it.
In the thing file, this looks e.g. like

```
Bridge sinope:gateway:home [ hostname="[YOUR_GATEWAY_IP_OR_HOSTNAME]", gatewayId="[YOUR_GATEWAY_ID]", apiKey="0x1F 0x5D 0xC8 0xD5 0xCD 0x3A 0xD7 0x23"]
```

The devices are identified by the ids that a Sinopé device returns when you have discovered it.

```
thermostat room [ deviceId = "0x00 0x00 0x35 0x86" ]
dimmer room [ deviceId = "0x00 0x00 0x35 0x87" ]
```

## Channels

Thermostat devices support some of the following channels:

 Channel Type ID     | Item Type   | Description                                                                                                                            
---------------------|-------------|----------------------------------------------------------------------------------------------------------------------------------------|
 insideTemperature   | Number (R)  | Inside Temperature                                                                                                                     |   
 outsideTemperature  | Number (R)  | Outside Temperature                                                                                                                    | 
 setpointTemperature | Number (RW) | Set Point Temperature                                                                                                                  | 
 setpointMode        | String (RW) | Thermostat set point mode                                                                                                              |     
 heatingLevel        | Number (R)  | Heating Level                                                                                                                             | 

Dimmer/Switch devices support the following channels:

 Channel Type ID         | Item Type   | Description                                                                                                                                                                                                   |
-------------------------|-------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
 dimmerOutputIntensity   | Number (RW) | Output Intensity (0 = OFF, 100 = Full Intensity). For switches, 0 = OFF, 100 = ON                                                                                                                             |   
 lightMode               | Number (RW) | Light Mode (1 = Manual (Hold), 2 = Auto (Schedule), 3 = Random (Simulation of presence), 130 = Bypass Auto (Temporary hold until the next scheduled period)                                                   |    


## Full Example

In this example setup the Sinopé Gateway is represented as a Bridge **Home** with thermostat **Room** and dimmer "RoomDimmer"

### demo.things:

```
Bridge sinope:gateway:home [ hostname="sinope", gatewayId="1234-4567-1234-1234", apiKey="0x12 0x34 0x56 0x78 0x9A 0xBC 0xDE 0xF0"] {
  thermostat room [ deviceId = "00003586" ]
  dimmer roomdimmer [ deviceId = "00003587" ]
}
```

### demo.items:

```
Number Room_In  "Room Temp. [%.2f °C]" <temperature> { channel="sinope:thermostat:home:room:insideTemperature" }
Number Room_Out "Outside Temp. [%.2f °C]" <temperature> { channel="sinope:thermostat:home:room:outsideTemperature" }
Number Room_SetPoint "Room Set Point [%.2f °C]" <temperature> { channel="sinope:thermostat:home:room:setpointTemperature" }
Number Room_SetPointMode "Room Set Point Mode" { channel="sinope:thermostat:home:room:setpointMode" }
Number Room_HeatLevel "Room Heating level [%d]" <heating> { channel="sinope:thermostat:home:room:heatingLevel" }

Number Room_Dimmer_OutputIntensity "Dimmer Output Intensity [%d] %%" <light> { channel="sinope:dimmer:home:roomdimmer:dimmerOutputIntensity" }
Number Room_Dimmer_LightMode "Dimmer Light Mode [%d] <light> { channel="sinope:dimmer:home:roomdimmer:lightMode" }
```

### demo.sitemap:

```
sitemap demo label="Main Menu"
{ 
  Frame label="Sinope" {
     Text item=Room_In   
     Text item=Room_Out
     Setpoint item=Room_SetPoint  label="Set Point [%.1f °C]" step=0.5 minValue=5 maxValue=35
     Switch item=Room_SetPointMode mappings=[2=Manual, 3=Auto, 5=Away]
     Slider item=Room_HeatLevel

     Slider item=Room_Dimmer_OutputIntensity label="Dimmer Ouput Intensity"
     Switch item=Room_Dimmer_LightMode label="Dimmer Light Mode" mappings=[1=Manual, 2=Auto, 3=Random, 130=BypassAuto]
  }
}
```

### UI Example

![Example](doc/openhab.png)
