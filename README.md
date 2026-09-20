# Laptop Battery Charge controller
This is my Grade 12 Information Technology practical project, for which I achieved a mark of 100% for.

The purpose of this project is to keep your laptop battery below a threshold percentage, say 80%, while left on the charger, in order to extend the longevity of the battery. While some laptops include options in the BIOS to configure this, it is not universal and often not particularly versatile

This project aims to take a more external approach that will work on any type of laptop. To use it you connect your charger through a Shelly smart relay, which the program running on your computer connects to over the network. Then, when the battery hits your chosen limit, it sends a network command to the Shelly to cut the power, and then when the battery drops, it sends another command to turn the power back on, in a continuous cycle until you take the laptop off the charger.

This approach removes all requirements for complicated proprietary APIs by simply relying on the charge information Windows makes available in conjunction with a physical device.
