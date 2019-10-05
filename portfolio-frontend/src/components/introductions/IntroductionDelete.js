import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import { withStyles } from '@material-ui/styles';
import React from 'react';
import * as service from '../../service/introduction';

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

    deleteIntroduction = async () => {
        const no = this.props.idx;
        await service.deleteIntroduction(no);
        this.props.handleClickClose()
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
                <Button  color="primary" className={classes.button} onClick={this.handleClickOpen}>
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
                            {this.props.introductionTitle} 을 삭제하시겠습니까?
                </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            아니오
                        </Button>
                        <Button onClick={this.deleteIntroduction}  color="primary" autoFocus>
                            예
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(IntroductionDelete);