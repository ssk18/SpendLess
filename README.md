```markdown
# SpendLess App

## Overview

The Transactions App is a mobile application built using Kotlin and Gradle. It allows users to manage their financial transactions, categorize expenses, and track recurring payments.

## Features

*   **Transaction Management:** Add, edit, and delete transactions.
*   **Expense Categorization:** Categorize expenses into predefined categories such as Home, Food, Entertainment, etc.
*   **Recurring Transactions:** Set up recurring transactions with daily, weekly, monthly, or yearly repetition.
*   **User Interface:** A user-friendly interface built with Jetpack Compose.

## Technologies Used

*   Kotlin
*   Java
*   Gradle
*   Jetpack Compose
*   Other libraries (list any other relevant libraries)

## Project Structure

The project is structured into several modules:

*   `core`: Contains core functionalities and data models.
    *   `core/domain`: Defines the domain layer, including entities like `Transaction`, `Expense`, and `RepeatType`.
*   `transactions`: Contains presentation layer
    *   `transactions/presentation`: Implements the UI and presentation logic using Jetpack Compose.
    *   `transactions/presentation/src/main/java/com/serhiiromanchuk/transactions/common_components`: Reusable UI components such as `TransactionDropdownCategory.kt`.
    *   `transactions/presentation/src/main/java/com/serhiiromanchuk/transactions/utils`: Utility functions, including mappers for converting between UI and domain layer models (`TransactionMapper.kt`).

## Getting Started

### Prerequisites

*   Android Studio Meerkat | 2024.3.1
*   Android SDK
*   Java Development Kit (JDK)

### Installation

1.  Clone the repository:

    ```bash
    git clone <repository_url>
    ```

2.  Open the project in Android Studio.
3.  Build the project:

    ```bash
    ./gradlew build
    ```

4.  Run the application on an emulator or a physical device.
