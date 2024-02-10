# Cryptocurrency Price Prediction using LSTM and Sentiment Analysis

## Overview
This project aims to leverage machine learning techniques, specifically Long Short-Term Memory (LSTM), to predict cryptocurrency prices based on sentiment analysis derived from social media platforms such as Reddit and trending news articles. Cryptocurrency prices are known to be highly speculative, often driven by consensus and trends within online communities. By integrating real-time data from various sources including Reddit posts, cryptocurrency prices, and news articles, our system provides users with predictive insights into the volatile cryptocurrency market.

## Methodology
1. **Data Collection**: Our backend system gathers data from multiple sources including Reddit posts, cryptocurrency prices (e.g., Bitcoin, Litecoin, Ethereum), and trending news platforms.
2. **Sentiment Analysis**: Natural Language Processing (NLP) techniques are employed to analyze the sentiment of Reddit posts and news articles related to cryptocurrencies. This sentiment analysis contributes to understanding market sentiment and trends.
3. **LSTM Model**: We implement an LSTM model to predict cryptocurrency prices based on the recent 15 days of data entries. LSTM is a type of recurrent neural network (RNN) architecture capable of capturing long-term dependencies in sequential data, making it well-suited for time series forecasting tasks.
4. **Database and Deployment**: The system operates on a Linux Ubuntu instance hosted on Microsoft Azure. Real-time data updates and predictions are stored and retrieved from Google Firebase Firestore database.
5. **Android Application**: We have developed an Android application to display the speculative price predictions, current cryptocurrency prices, manage portfolios, access articles related to cryptocurrencies, and receive notifications for significant price movements.

## Features
- **Cryptocurrency Price Prediction**: Utilizing LSTM and sentiment analysis to forecast cryptocurrency prices.
- **Real-time Updates**: Continuous updates from multiple data sources ensure that predictions reflect the latest market sentiment.
- **User-friendly Interface**: The Android application provides an intuitive interface for users to access predictions, manage portfolios, and stay informed about the cryptocurrency market.
- **Notification System**: Users receive alerts for significant bullish trends in cryptocurrency prices.


## Contributions
Contributions to this project are welcome. If you have any suggestions, bug reports, or feature requests, please feel free to open an issue or submit a pull request.

## License
This project is licensed under the [MIT License](LICENSE).

---

*Note: This project is for educational and experimental purposes only. Cryptocurrency trading involves risks, and predictions provided by this system should not be considered financial advice.*
