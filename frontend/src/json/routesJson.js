import Loadable from "react-loadable";
import PageLoader from "../components/layouts/PageLoader";

export const routesJson = [
  {
    key: "comp1",
    path: "/",
    exact: true,
    component: Loadable({
      loader: () => import("../routes/login/LoginPage"),
      loading: PageLoader
    })
  }
];
