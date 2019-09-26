import React from 'react';
import { withStyles } from '@material-ui/core';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Slide from '@material-ui/core/Slide';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import axios from 'axios';

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

class IntroductionAdd extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            open: false,
            title: '',
            reason: '',
            strength: '',
            weakness: '',
            aspiration: '',
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleFormSave = (e) => {
        e.preventDefault()

    }

    editIntroduction = () => {
        axios.post('http://localhost:8080/api/introductions', this.state)
            .then(res => {
                console.log(res);
            })
    }

    handleClickOpen = () => {
        this.setState({
            open: true,
            title: this.props.title,
            growth: this.props.growth,
            reason: this.props.reason,
            strength: this.props.strength,
            weakness: this.props.weakness,
            aspiration: this.props.aspiration
        })
    }

    handleClose = (event) => {
        this.editIntroduction();
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
                <Button variant="contained" color="primary" className={classes.button} onClick={this.handleClickOpen}>
                    add
                </Button>
                <Dialog fullScreen open={this.state.open} onClose={this.handleClose} TransitionComponent={Transition}>
                    <AppBar className={classes.appBar}>
                        <Toolbar>
                            <IconButton edge="start" color="inherit" onClick={this.handleClose} aria-label="close">
                                <CloseIcon />
                            </IconButton>
                            <Typography variant="h6" className={classes.title}>
                                <textarea name="title" onChange={this.handleChange} />   
                            </Typography>
                            <Button color="inherit" onClick={this.handleClose}>
                                save
                            </Button>
                        </Toolbar>
                    </AppBar>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant="h6" component="h2">
                                성장과정
                            </Typography>
                            <br />
                            <textarea name="growth" value={this.state.growth} onChange={this.handleChange} />

                        </CardContent>
                    </Card>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant="h6" component="h2">
                                지원 동기
                            </Typography>
                            <br />
                            <textarea name="reason" value={this.state.reason} onChange={this.handleChange} />
                        </CardContent>
                    </Card>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant="h6" component="h2">
                                장점
                            </Typography>
                            <br />
                            <textarea name="strength" value={this.state.strength} onChange={this.handleChange} />
                        </CardContent>
                    </Card>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant="h6" component="h2">
                                단점
                            </Typography>
                            <br />
                            <textarea name="weakness" value={this.state.weakness} onChange={this.handleChange} />
                        </CardContent>
                    </Card>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant="h6" component="h2">
                                입사 후 포부
                            </Typography>
                            <br />
                            <textarea name="aspiration" value={this.state.aspiration} onChange={this.handleChange} />
                        </CardContent>
                    </Card>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(IntroductionAdd);