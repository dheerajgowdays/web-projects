import express from "express"

const app = express()

app.getMaxListeners("/api/")

app.listen(5000,()=>{
    console.log("Server started on Port: 5001");
});