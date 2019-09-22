import { withStyles } from '@material-ui/core/styles';
import React from 'react';
import './App.css';
import DialogFull from './components/DialogFull';
import Home from './pages/Home';
import Introduction from './pages/Introduction';
import Projects from './pages/Projects';

const styles = (theme) => ({
  button: {
    margin: theme.spacing(1),
    color: 'white'
  }
});

class App extends React.Component {

  handleProjectsButton = () => {
    this.setState({
      page: 'Projects'
    })
  }

  handleIntroductionButton = () => {
    console.log("들왔남")
    this.setState({
      page: 'Introduction'
    })
  }

  handleHomeButton = () => {
    this.setState({
      page: 'Home'
    })
  }

  handleClickOpen = () => {
    this.setState({
      open: true
    })
  }

  handleClose = () => {
    this.setState({
      open: false
    })
  }

  state = {
    open: false,
    page: 'Home'
  }

  render() {
    return (
      <div>
        {
          (() => {
            if (this.state.page === 'Home') return (
              < div className="App-Background" >
                <DialogFull
                  page={this.state.page}
                  handleIntroductionButton={this.handleIntroductionButton}
                  handleProjectsButton={this.handleProjectsButton}
                  handleHomeButton={this.handleHomeButton}
                />
              </div>)
            else return <DialogFull
              page={this.state.page}
              handleIntroductionButton={this.handleIntroductionButton}
              handleProjectsButton={this.handleProjectsButton}
              handleHomeButton={this.handleHomeButton}
            />
          })()
        }

        <div>
          {
            (() => {
              if (this.state.page === 'Home')
                return <Home
                  page={this.state.page}
                  handleIntroductionButton={this.handleIntroductionButton}
                  handleProjectsButton={this.handleProjectsButton}
                  handleHomeButton={this.handleHomeButton}
                />
              if (this.state.page === 'Projects') return <Projects />
              if (this.state.page === 'Introduction') return <Introduction />
            })()
          }
        </div>
      </div >
    );
  }
}

export default withStyles(styles)(App);