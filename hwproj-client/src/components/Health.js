import React from "react";
import "../css/Background.scss";
import formStyle from "../css/Form.module.scss";
import Sidebar from "./Sidebar.js";

const axios = require("axios").default;
const HEALTH_PATH = "health";
const SERVER_HEALTH_URL = process.env.REACT_APP_SERVER_API_URL + HEALTH_PATH;

export async function health() {
    let health = null;
    await axios.get(SERVER_HEALTH_URL).then(result => {
        health = result.data;
    }).catch(error => {
        health = error;
    });
    return health;
}

export default class Health extends React.Component {
    getHealth = () => {
        console.log(SERVER_HEALTH_URL)
        health().then(result => {
            alert(result);
        });
    };

    render() {
        return (
            <div className="Health">
                <Sidebar/>
                <div id="bg"/>
                <div className={formStyle.form}>
                    <button className={formStyle.btn} onClick={this.getHealth}>
                        health
                    </button>
                </div>
            </div>
        );
    }
}
