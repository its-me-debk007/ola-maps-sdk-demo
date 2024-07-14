# OlaMaps SDK Demo

Trying out the OlaMaps Android SDK

## Getting Started 👣

- Clone the repo
- Add your `CLIENT_ID` and `CLIENT_SECRET` in `local.properties`:
  
  ```
  CLIENT_ID=abc123
  CLIENT_SECRET=abc123
  API_KEY=abc123
  ```
- Go to `Build` -> `Clean Project`
- Ensure your device has `Location` enabled
- Uninstall any existing version of the app
- Run the app

## Features ✨
- `access token` valid for 60 minutes. Implemented logic to auto-refresh token when expired or absent
- Implemented some features that are not even included in official documentation
- Jump to current location with a button click
- An Info card (called HuddleMarkerView) for current location
- Choose location by search or tap on the map
- Get directions from current location to chosen destination

> [!NOTE]
> This is an ongoing project, and I'm exploring more features available in the OlaMaps SDK

## Demo
https://github.com/user-attachments/assets/7ac1a6cc-463d-4f41-a073-1ba4d464d0d4

> [!NOTE]
> Map not rendering on Emulator? Try real device

## License

Distributed under the MIT License. See `LICENSE` file for more information.

#### 🌟 If you find value in this project, please consider starring it! Your support keeps it thriving. 🚀
