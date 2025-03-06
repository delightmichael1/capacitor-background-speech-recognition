// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "SpeechToText",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "SpeechToText",
            targets: ["SpeechToTextPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "SpeechToTextPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SpeechToTextPlugin"),
        .testTarget(
            name: "SpeechToTextPluginTests",
            dependencies: ["SpeechToTextPlugin"],
            path: "ios/Tests/SpeechToTextPluginTests")
    ]
)