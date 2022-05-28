import React from "react";
import {slide as Menu} from "react-burger-menu";
import "../css/Sidebar.scss";
import {NavLink} from "react-router-dom";

export default class Sidebar extends React.Component {
    render() {
        return (
            <Menu width={"280px"} disableAutoFocus>
                <NavLink className="menu-item" to="/">Home</NavLink>
                <NavLink className="menu-item" to="/health">Health</NavLink>
            </Menu>
        );
    }
}
