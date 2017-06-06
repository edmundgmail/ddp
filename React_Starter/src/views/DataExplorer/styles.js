'use strict';

export default {
    component: {
        width: '50%',
        display: 'inline-block',
        verticalAlign: 'top',
        padding: '20px',
        '@media (max-width: 640px)': {
            width: '100%',
            display: 'block'
        }
    },
    searchBox: {
        padding: '20px 20px 0 20px'
    },
    viewer: {
        base: {
            fontSize: '12px',
            whiteSpace: 'pre-wrap',
            backgroundColor: 'default',
            border: 'solid 1px black',
            padding: '20px',
            color: '#222222',
            minHeight: '250px'
        }
    }
};