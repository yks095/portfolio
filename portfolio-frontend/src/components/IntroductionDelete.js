import React from 'react';
import { withStyles } from '@material-ui/styles';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import axios from 'axios';

const styles = theme => ({
    button: {
        margin: 10,
        float: 'right'
    }
});

class IntroductionDelete extends React.Component {

    state = {
        open: false,
    }

    deleteIntroduction = () => {
        const no = this.props.idx;
        axios.delete('http://localhost:8080/api/introductions/' + no)
            .then(res => {
                console.log(res);
            })
        this.setState({
            open: false
        })
    }

    handleClickOpen = () => {
        this.setState({
            open: true
        })
    }

    handleClose = () => {
        this.setState({
            open: false,
        })
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <Button variant="contained" color="primary" className={classes.button} onClick={this.handleClickOpen}>
                    delete
            </Button>
                <Dialog
                    open={this.state.open}
                    onClose={this.handleClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                >
                    <DialogTitle id="alert-dialog-title">{"Delete"}</DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            {this.props.title} 을 삭제하시겠습니까?
                </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            아니오
                        </Button>
                        <Button onClick={this.deleteIntroduction} color="primary" autoFocus>
                            예
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(IntroductionDelete);