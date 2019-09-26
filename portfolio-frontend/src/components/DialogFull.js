import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import IconButton from '@material-ui/core/IconButton';
import Slide from '@material-ui/core/Slide';
import { withStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import MenuButton from '@material-ui/icons/Menu';
import React from 'react';

const styles = (theme) => ({
    root: {
        float: 'center',
        textAlign: 'center',
        margin: 'auto'

    },
    dialogButton: {
        float: 'center',
        position: 'relative',
        fontSize: 55,
        fontWeight: 'bold',
        margin: '5%',
        width: '100%'
    },
    appBar: {
        position: 'relative',
    },
    title: {
        marginLeft: theme.spacing(2),
        flex: 1,
    },
    button: {
        margin: theme.spacing(1),
        // float: 'right'
    },
    input: {
        display: 'none',
    }
});

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

class DialogFull extends React.Component {

    handleClickOpen = () => {
        this.setState({
            open: true
        })
    }

    handleClose = () => {
        this.setState({
            open: false
        })
    };

    state = {
        open: false,
    };

    render() {
        const { classes } = this.props;
        return (
            <div>
                <IconButton className={classes.button} onClick={this.handleClickOpen}>
                    <MenuButton fontSize="large" />
                </IconButton>
                <Dialog className={classes.appBar} fullScreen open={this.state.open} onClose={this.handleClose} TransitionComponent={Transition}>
                    <div>
                        <IconButton className={classes.button} edge="start" color="inherit" onClick={this.handleClose} aria-label="close">
                            <CloseIcon fontSize="large" />
                        </IconButton>
                    </div>
                    <div className={classes.root}>
                        <Button className={classes.dialogButton} href="/">MAIN</Button>
                        <br />
                        <Button className={classes.dialogButton}>마이 페이지</Button>
                        <br />
                        <Button className={classes.dialogButton}>회원정보 수정</Button>
                        <br />
                        <Button className={classes.dialogButton} href="/signIn">로그아웃</Button>
                    </div>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(DialogFull);