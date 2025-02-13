import './App.css';


function App() {

  return (
      <>
        <span v-if="hasAuthority">
          <slot/>
        </span>
        <span v-else>
          {{fallbackText}}
        </span>
      </>
  )
}

export default App
