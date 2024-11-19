import { Link, Outlet } from "react-router-dom";
import logo from "./assets/hoaxify.png";
import { LanguageSelector } from "./shared/components/LanguageSelector";
import { useTranslation } from "react-i18next";
import { NavBar } from "./shared/components/NavBar";

function App() {
  const { t } = useTranslation();

  return (
    <>
      <NavBar />
      <div className='container mt-3'>
        <Outlet />
        <LanguageSelector />
      </div>

    </>
  );
}

export default App;
