import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Home from './pages/home/Home.tsx';
import Navbar from "./components/navbar/Navbar.tsx"


function App() {

  return (
      <>
        <Navbar/>

        {/*TODO: Routes (placeholder home page for now)*/}
        <Home/>
      </>
  )
}

export default App
