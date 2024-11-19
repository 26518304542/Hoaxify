import { createBrowserRouter } from "react-router-dom";

import { Home } from "../pages/Home";
import { SignUp } from "../pages/SignUp";
import {Activation} from "../pages/Activation";
import App from "../App";


export default createBrowserRouter([
    {
    path: "/",
    Component: App,
    children: [
        {
            path: "/",
            index: true,
            Component: Home,
        },
        {
            path: "/signup",
            Component: SignUp,
        },
        {
            path: "/activation/:token",
            Component: Activation,
        }
    ]
}
])