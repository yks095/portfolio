import React from 'react';
import { Route } from 'react-router-dom';
import { BrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import Introduction from './pages/Introduction';
import Projects from './pages/Projects';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import './App.css';

class App extends React.Component {
  render() {
    return (
      <div>
        <BrowserRouter>
          <Route exact path="/" component={Home} />
          <Route path="/signIn" component={SignIn} />
          <Route path="/signUp" component={SignUp} />
          <Route path="/introductions" component={Introduction} />
          <Route path="/projects" component={Projects} />
        </BrowserRouter>
      </div>
    );
  }
}

export default App;