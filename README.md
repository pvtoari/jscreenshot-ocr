<p align="center">
  <a href="" rel="noopener">
 <img width=200px height=200px src="./media/projectLogo.png" alt="Project logo"></a>
</p>

<h3 align="center">JScreenshot-OCR</h3>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]()
[![GitHub Issues](https://img.shields.io/github/issues/pvtoari/java_keylogger.svg)](https://github.com/pvtoari/java_keylogger/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/pvtoari/java_keylogger.svg)](https://github.com/pvtoari/java_keylogger/pulls)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](/LICENSE)

</div>

## Table of Contents

- [Table of Contents](#table-of-contents)
- [About](#about)
  - [Prerequisites](#prerequisites)
  - [Installing](#installing)
- [Usage](#usage)
- [Built Using](#built-using)
- [Authors](#authors)
- [Contributing](#contributing)
- [License](#license)

## About

JScreenshot-OCR is a Java-based application designed to capture screenshots and perform Optical Character Recognition (OCR) on the captured images using the Tesseract OCR library. This tool is particularly useful for extracting text from images quickly and efficiently, making it an invaluable asset for tasks such as document digitization and information extraction from images.

### Prerequisites

* Java 17 or greater.
* Internet connection (not always required).

### Installing

1. **Clone the repository:**

    ```sh
    git clone https://github.com/yourusername/JScreenshot-OCR.git
    cd JScreenshot-OCR
    ```

2. **Build the project using Maven:**

    ```sh
    mvn clean package
    ```

The compiled project will appear as a JAR file named ``javascreenshotOCR-jar-with-dependencies.jar`` at ``project/target``

## Usage

1. **Launch the application:**

    ```sh
    java -jar target/javascreenshotOCR-jar-with-dependencies.jar
    ```

2. **Capture a screenshot:**
    - Click the "Capture Screen" button to start capturing the screen.

3. **Perform OCR:**
    - The captured image will be processed, and the OCR result will be displayed in the text pane.

4. **Save OCR results:**
    - Use the "File" menu to save the OCR results or the captured image to a file.
  
5. **Use the menu bar features:**
    - Import image as a source to perform OCR.
    - Clearing content, screenshot or both from the GUI.
    - Access to "About", reporting issues via GitHub, etc..
    - (WIP) Configuration menu and multilanguage support.

## Built Using

- **Tess4J:** The project uses the [Tess4J]() OCR library to perform text recognition on images.

    ```xml
    <dependency>
        <groupId>net.sourceforge.tess4j</groupId>
        <artifactId>tess4j</artifactId>
        <version>5.13.0</version>
    </dependency>
    ```
- [Maven](https://maven.apache.org/) - Dependency management

## Authors

- [@pvtoari](https://github.com/pvtoari) - Main developer

See also the list of [contributors](https://github.com/pvtoari/java_keylogger/contributors) who participated in this project.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

1. Fork the repository.
2. Create a new branch: `git checkout -b my-feature-branch`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin my-feature-branch`.
5. Submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
