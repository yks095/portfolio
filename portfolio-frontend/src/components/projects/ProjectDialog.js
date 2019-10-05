import { Button } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Slide from '@material-ui/core/Slide';
import ProjectDelete from './ProjectDelete';
import ProjectEdit from './ProjectEdit';

const styles = theme => ({
    panel: {
        marginBottom: '5px'
    },
    root: {
        width: '100%',
    },
    card: {
        minWidth: '100%',
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    button: {
        margin: 10,
        width: '100%',
        height: '100%',
        fontSize: '300%',
        fontWeight: 'bold',
        fontFamily: 'Nanum Brush Script'

    }
});

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

class ProjectDialog extends React.Component {
    constructor(props) {
        super();
        this.state = {
            name: "",
            description: "",
            image: "",
            period: "",
            git_addr: "",
            registeredDate: "",
            open: false
        }
    }

    handleClickOpen = () => {
        this.setState({
            open: true
        })
    }

    handleClickClose = () => {
        this.setState({
            open: false
        })
    }

    componentDidMount = () => {
        this.setState({
            name: this.props.name,
            description: this.props.description,
            image: this.props.image,
            period: this.props.period,
            git_addr: this.props.git_addr,
            registeredDate: this.props.registeredDate
        })
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <Button className={classes.button} onClick={this.handleClickOpen} variant="contained">{this.state.name}</Button>
                <Dialog
                    fullWidth
                    maxWidth="lg"
                    open={this.state.open}
                    onClose={this.handleClose}
                    TransitionComponent={Transition}
                >
                    <DialogTitle>프로젝트 명</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            {this.props.name}
                        </DialogContentText>
                    </DialogContent>
                    <DialogTitle>프로젝트 설명</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            {this.props.description}
                        </DialogContentText>
                    </DialogContent>
                    <DialogTitle>프로젝트 기간</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            {this.props.period}
                        </DialogContentText>
                    </DialogContent>
                    <DialogTitle>등록일</DialogTitle>
                    <DialogContent>
                        <DialogContentText>
                            {this.props.registeredDate}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <ProjectEdit
                            idx = {this.props.idx}
                            name = {this.props.name}
                            description = {this.props.description}
                            period = {this.props.description}
                        />
                        <ProjectDelete 
                            idx = {this.props.idx}
                            name = {this.props.name}
                            handleClickClose = {this.handleClickClose}
                        />
                        <Button onClick={this.handleClickClose} color="primary" autoFocus>
                            Close
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(ProjectDialog);