import { useState } from "react";

function login(){
    const [email,setEmail] = useState("");
    const [password,setPassword] = useState("");

    const handelSubmit = (e) =>{
        e.preventDefault();
        console.log("Email:",email);
        console.log("Password:",password);
        return{
            <div className="min-h-screen flex items-center justify-center bg-gray-100"> 
            <div className="bg-white p-8 rounded-2xl shadow-xl w-96">
            <h2 className="text-3xl font-bold text-center text-red-500 mb-6"></h2>
            
            </div>
            </div>
        }
    }
}