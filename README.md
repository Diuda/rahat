# rahat-cfd

**Network Connectivity** and **Communication** with the outside world is one of the major issues faced during Natural disasters. In our idea, we make an attempt to bridge this gap. People are often trapped and unable to reach out for help, things get worse in absence of network Thus the solution we propose is a P2P network, which will work in the absence of any Internet or Cellular Network connectivity on cellphones. This would be implemented using Google Nearby API, which would use a combination of BLE and WiFi.Nearby Connections is a peer-to-peer networking API that allows apps to easily discover, connect to, and exchange data with nearby devices in real-time, regardless of network connectivity. 

The network will connect the trapped victims, allowing them to communicate and help each other. Moreover, the volunteers/bots(can be used in locations which are not accessible) who patrol the affected area would also serve as an active part of the network - allowing the victims to reach out to them through the app. The strength and range of the network will be decided by the users (volunteers and victims) present in it.
The other issue we're trying to solve is locating the victim. But, in cases where people might be trapped(indoors/debris), GPS might not be readily available. We'll try to pin point the location of victims through triangulaion using the volunteers' location as reference.

Also, when internet/cellular network is finally accessible by any of the peers, the communication details will be pushed to the **Azure server** where the distress message can be relayed to the relief authorities and families. As an added functionality, we'll plot the location of the user on a globally available map, allowing authorities to locate and help the victim easily.

