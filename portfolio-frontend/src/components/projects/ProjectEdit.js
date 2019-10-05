import { withStyles } from '@material-ui/core';
import AppBar from '@material-ui/core/AppBar';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import IconButton from '@material-ui/core/IconButton';
import Slide from '@material-ui/core/Slide';
import TextField from '@material-ui/core/TextField';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import React from 'react';
import * as service from '../../service/projects';

const styles = theme => ({
    button: {
        margin: 10,
        float: 'right'
    },
    appBar: {
        position: 'relative',
    },
    title: {
        marginLeft: theme.spacing(2),
        flex: 1,
    }
});

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

class ProjectEdit extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            open: false,
            idx: '',
            name: "",
            description: "",
            period: "",
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleFormSave = (e) => {
        e.preventDefault()

    }

    editProject = async () => {
        const no = this.state.idx;
        console.log(this.state);
        await service.editProject(no, this.state);
    }

    handleClickOpen = () => {
        this.setState({
            open: true,
            idx: this.props.idx,
            name: this.props.name,
            description: this.props.description,
            period: this.props.period,
        })
    }

    handleClose = (event) => {
        console.log(this.state)
        this.editProject();
        this.setState({
            open: false
        })
    }

    handleChange = (event) => {
        const target = event.target
        const name = target.name
        const value = target.value

        this.setState({
            [name]: value
        });
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <Button color="primary" className={classes.button} onClick={this.handleClickOpen}>
                    edit
                </Button>
                <Dialog
                    fullWidth
                    maxWidth="lg"
                    open={this.state.open}
                    onClose={this.handleClose}
                    TransitionComponent={Transition}
                >
                    <AppBar className={classes.appBar}>
                        <Toolbar>
                            <IconButton edge="start" color="inherit" onClick={this.handleClose} aria-label="close">
                                <CloseIcon />
                            </IconButton>
                            <Typography variant="h6" className={classes.title}>
                                <TextField label="ProjectName" value={this.state.name} autoFocus margin="dense" name="name" onChange={this.handleChange} />
                            </Typography>
                            <Button color="inherit" onClick={this.handleClose}>
                                save
                            </Button>
                        </Toolbar>
                    </AppBar>
                    <div className={classes.textField}>
                        <TextField label="Description" fullWidth autoFocus margin="dense" name="description" value={this.state.description} onChange={this.handleChange} />
                        <TextField label="period" autoFocus margin="dense" name="period" value={this.state.period} onChange={this.handleChange} />
                    </div>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(ProjectEdit);