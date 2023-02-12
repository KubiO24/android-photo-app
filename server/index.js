const express = require("express");
const app = express();
const port = 3000;

const formidable = require("formidable");

app.get("/", (req, res) => {
    res.send("Serwer :)");
});

app.post("/upload", (req, res) => {
    console.log("upload");
    let form = formidable({});
    form.keepExtensions = true;
    form.uploadDir = __dirname + "/static/upload/"; // folder do zapisu zdjęcia

    form.parse(req, function (err, fields, files) {
        res.send({ status: "plik przesłany!" });
    });
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`);
});
