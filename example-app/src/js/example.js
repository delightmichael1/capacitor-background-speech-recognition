import { SpeechToText } from 'speech-to-text';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    SpeechToText.echo({ value: inputValue })
}
