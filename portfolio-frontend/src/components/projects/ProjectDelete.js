import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import React from 'react';
import * as service from '../../service/projects';

class Project extends React.Component {

    state = {
        open: false,
    }

    deleteProject = async () => {
        const no = this.props.idx;
        await service.deleteProject(no);
        this.props.handleClickClose();
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
        return (
            <div>
                <Button  color="primary" onClick={this.handleClickOpen}>
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
                            {this.props.name} 을 삭제하시겠습니까?
                </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose} color="primary">
                            아니오
                        </Button>
                        <Button onClick={this.deleteProject}  color="primary" autoFocus>
                            예
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default Project;