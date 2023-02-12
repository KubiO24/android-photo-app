const express = require("express");
const app = express();
const port = 3000;
const fs = require("fs");
const formidable = require("formidable");

const filesFolder = "./public/upload/";

app.use(express.static("public"));

app.get("/", (req, res) => {
    res.send("Serwer :)");
});

app.get("/json", (req, res) => {
    res.type("json");
    const json = [];
    fs.readdirSync(filesFolder).forEach((file) => {
        const stats = fs.statSync(filesFolder + file);
        const name = file;
        const url = "/upload/" + name;
        const creationTime = stats.birthtime;
        const size = stats.size;
        json.push({
            name: name,
            url: url,
            creationTime: creationTime,
            size: size,
        });
    });
    res.send(json);
});

app.post("/upload", (req, res) => {
    let form = formidable({});
    form.keepExtensions = true;
    form.uploadDir = __dirname + "/public/upload/"; // folder do zapisu zdjęcia

    form.parse(req, function (err, fields, files) {
        res.send({ status: "plik przesłany!" });
    });
});

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`);
});
