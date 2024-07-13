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

## Ongoing Project 🚧

- This is an ongoing project, and I am exploring more features available in the official OlaMaps SDK
- For now, the app shows the map around your current location
- The `access token` is valid for 60 minutes. Logic has been implemented to automatically fetch a new token when it is absent or expired

### Route
This app has a route enabled, that starts form your current location to Hauz Khas, Delhi, India

## Screenshot
<img src="https://github.com/user-attachments/assets/08211fb0-9838-49a6-a5bc-cea02f77ef9f" width="300">

> [!NOTE]  
> Ensure that the device does not have proxy certificate installed, or you'll get an `Unacceptable certificate` error.

## License

Distributed under the MIT License. See `LICENSE` file for more information.

#### 🌟 If you find value in this project, please consider starring it! Your support keeps it thriving. 🚀
